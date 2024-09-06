package com.project.ens.service.impl;

import com.project.ens.dto.NewUserDto;
import com.project.ens.dto.UserDto;
import com.project.ens.exception.ConflictException;
import com.project.ens.exception.NotFoundException;
import com.project.ens.mapper.UserMapper;
import com.project.ens.model.User;
import com.project.ens.model.UserRole;
import com.project.ens.repository.UserRepository;
import com.project.ens.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public UserDto create(NewUserDto newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new ConflictException("Пользователь с email " + newUser.getEmail() + " уже существует");
        }

        User user = userMapper.toUser(newUser);
        user.setPassword(encoder.encode(newUser.getPassword()));
        user.setRole(UserRole.ROLE_USER);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new NotFoundException("Пользователь с email " + email + " не найден"));
        return userMapper.toUserDto(user);
    }
}
