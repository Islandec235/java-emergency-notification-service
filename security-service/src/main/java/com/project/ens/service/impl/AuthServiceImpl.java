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
import com.project.ens.service.AuthService;
import com.project.ens.service.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper mapper;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> refreshStorage;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder encoder;


    @Override
    @Transactional
    public JwtResponse login(JwtRequest authRequest) {
        final User user = userRepository.findByEmail(authRequest.getLogin())
                .orElseThrow(() ->
                        new NotFoundException("Пользователь с email " + authRequest.getLogin() + " не найден"));

        if (!encoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Неверный пароль");
        }

        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.opsForValue().set(user.getEmail(), refreshToken, 3, TimeUnit.DAYS);
        return new JwtResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public Boolean register(NewUserDto newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isEmpty()) {
            User user = mapper.toUser(newUser);
            user.setPassword(encoder.encode(newUser.getPassword()));
            userRepository.saveAndFlush(user);
            return true;
        } else {
            throw new ConflictException("Пользователь с этим Email уже существует");
        }
    }

    @Override
    @Transactional
    public JwtResponse getAccessToken(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = (String) refreshStorage.opsForValue().get(login);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userRepository.findByEmail(login)
                        .orElseThrow(() -> new NotFoundException("Пользователь с email " + login + " не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }

        return new JwtResponse(null, null);
    }

    @Override
    @Transactional
    public JwtResponse refresh(String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = (String) refreshStorage.opsForValue().get(login);

            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userRepository.findByEmail(login)
                        .orElseThrow(() -> new NotFoundException("Пользователь с email " + login + " не найден"));
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.opsForValue().set(user.getEmail(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new UnauthorizedException("Неверный JWT токен");
    }
}
