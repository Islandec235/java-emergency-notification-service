package com.project.ens.model.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtRequest {
    String login;
    String password;
}
