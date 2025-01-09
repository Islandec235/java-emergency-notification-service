package com.project.ens.service.impl;

import com.project.ens.dto.NewUserDto;
import com.project.ens.exception.ConflictException;
import com.project.ens.exception.NotFoundException;
import com.project.ens.exception.UnauthorizedException;
import com.project.ens.mapper.UserMapper;
import com.project.ens.model.User;
import com.project.ens.model.jwt.JwtRequest;
import com.project.ens.model.jwt.JwtResponse;
import com.project.ens.repository.UserRepository;
import com.project.ens.service.JwtProvider;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {
    @Mock
    private UserMapper mockMapper;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private RedisTemplate<String, Object> refreshStorage;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private AuthServiceImpl mockAuthService;

    @Test
    void testLoginSuccess() {
        JwtRequest authRequest = new JwtRequest();
        authRequest.setLogin("test@example.com");
        authRequest.setPassword("password");
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        ValueOperations<String, Object> valueOperations = mock(ValueOperations.class);

        when(mockUserRepository.findByEmail(authRequest.getLogin())).thenReturn(Optional.of(user));
        when(encoder.matches(authRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtProvider.generateAccessToken(user)).thenReturn("accessToken");
        when(jwtProvider.generateRefreshToken(user)).thenReturn("refreshToken");
        when(refreshStorage.opsForValue()).thenReturn(valueOperations);

        JwtResponse response = mockAuthService.login(authRequest);

        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(refreshStorage.opsForValue()).set(eq(user.getEmail()),
                eq("refreshToken"), eq(3L), eq(TimeUnit.DAYS));
    }

    @Test
    void testLoginUnauthorized() {
        JwtRequest authRequest = new JwtRequest();
        authRequest.setLogin("test@example.com");
        authRequest.setPassword("password");
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");

        when(mockUserRepository.findByEmail(authRequest.getLogin())).thenReturn(Optional.of(user));
        when(encoder.matches(authRequest.getPassword(), user.getPassword())).thenReturn(false);

        UnauthorizedException exception = assertThrows(UnauthorizedException.class,
                () -> mockAuthService.login(authRequest));
        assertEquals("Неверный пароль", exception.getMessage());
    }

    @Test
    void testLoginUserNotFound() {
        JwtRequest authRequest = new JwtRequest();
        authRequest.setLogin("notfound@example.com");
        authRequest.setPassword("password");

        when(mockUserRepository.findByEmail(authRequest.getLogin())).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> mockAuthService.login(authRequest));
        assertEquals("Пользователь с email notfound@example.com не найден", exception.getMessage());
    }

    @Test
    void testRegisterSuccess() {
        NewUserDto newUser = new NewUserDto();
        newUser.setEmail("newuser@example.com");
        newUser.setPassword("password");

        User user = new User();
        user.setEmail("newuser@example.com");

        when(mockUserRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(mockMapper.toUser(newUser)).thenReturn(user);
        when(encoder.encode(newUser.getPassword())).thenReturn("encodedPassword");

        Boolean result = mockAuthService.register(newUser);

        assertTrue(result);
        verify(mockUserRepository).saveAndFlush(user);
        verify(encoder).encode(newUser.getPassword());
    }

    @Test
    void testRegisterConflict() {
        NewUserDto newUser = new NewUserDto();
        newUser.setEmail("existingUser@example.com");

        when(mockUserRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.of(new User()));

        ConflictException exception = assertThrows(ConflictException.class, () -> mockAuthService.register(newUser));
        assertEquals("Пользователь с этим Email уже существует", exception.getMessage());
    }


    @Test
    void testRefreshInvalidToken() {
        String invalidToken = "invalidToken";

        when(jwtProvider.validateRefreshToken(invalidToken)).thenReturn(false);

        UnauthorizedException exception = assertThrows(UnauthorizedException.class,
                () -> mockAuthService.refresh(invalidToken));
        assertEquals("Неверный JWT токен", exception.getMessage());
    }
}
