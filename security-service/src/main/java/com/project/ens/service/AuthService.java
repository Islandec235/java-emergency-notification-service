package com.project.ens.service;

import com.project.ens.dto.NewUserDto;
import com.project.ens.model.jwt.JwtRequest;
import com.project.ens.model.jwt.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest authRequest);

    Boolean register(NewUserDto newUser);

    JwtResponse getAccessToken(String refreshToken);

    JwtResponse refresh(String refreshToken);
}
