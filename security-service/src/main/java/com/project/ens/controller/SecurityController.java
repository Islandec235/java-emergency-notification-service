package com.project.ens.controller;

import com.project.ens.dto.NewUserDto;
import com.project.ens.dto.RefreshJwtRequest;
import com.project.ens.model.jwt.JwtRequest;
import com.project.ens.model.jwt.JwtResponse;
import com.project.ens.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SecurityController {
    private final AuthService service;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Boolean registerNewUser(@RequestBody NewUserDto user) {
        log.info("Регистрация пользователя {}", user);
        return service.register(user);
    }

    @PostMapping("/login")
    public JwtResponse loginUser(@RequestBody JwtRequest dto) {
        log.info("Вход пользователя {}", dto);
        return service.login(dto);
    }

    @PostMapping("/token")
    public JwtResponse getNewAccessToken(@RequestBody RefreshJwtRequest request) {
        log.info("Получить новый access токен");
        return service.getAccessToken(request.getRefreshToken());
    }

    @PostMapping("/refresh")
    public JwtResponse getNewRefreshToken(@RequestBody RefreshJwtRequest request) {
        log.info("Получить новые токены");
        return service.refresh(request.getRefreshToken());
    }
}
