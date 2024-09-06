package com.project.ens.service;

import com.project.ens.model.jwt.JwtRequest;
import com.project.ens.model.jwt.JwtResponse;

public interface AuthService {
    JwtResponse login(JwtRequest authRequest);


}
