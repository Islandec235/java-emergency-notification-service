package com.project.ens.model.jwt;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
}
