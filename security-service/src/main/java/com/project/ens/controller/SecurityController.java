package com.project.ens.controller;

import com.project.ens.dto.NewUserDto;
import com.project.ens.dto.UserDto;
import com.project.ens.model.jwt.JwtRequest;
import com.project.ens.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SecurityController {
    private final AuthService service;

    @PostMapping("/register")
    public UserDto registerNewUser(@RequestBody NewUserDto user) {
        log.info("Регистрация пользователя {}", user);
        return service.register(user);
    }

    @PostMapping("/login")
    public UserDto loginUser(@RequestBody JwtRequest dto) {
        log.info("Вход пользователя {}", dto);
        return service.login(dto);
    }
}
