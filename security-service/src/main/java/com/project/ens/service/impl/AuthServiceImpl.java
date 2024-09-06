package com.project.ens.service.impl;

import com.project.ens.exception.ForbiddenException;
import com.project.ens.exception.NotFoundException;
import com.project.ens.model.User;
import com.project.ens.model.jwt.JwtRequest;
import com.project.ens.model.jwt.JwtResponse;
import com.project.ens.repository.UserRepository;
import com.project.ens.service.AuthService;
import com.project.ens.service.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;

    @Override
    public JwtResponse login(JwtRequest authRequest) {
        final User user = userRepository.findByEmail(authRequest.getLogin())
                .orElseThrow(() ->
                        new NotFoundException("Пользователь с email " + authRequest.getLogin() + " не найден"));
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getEmail(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new ForbiddenException("Неверный пароль");
        }
    }
}
