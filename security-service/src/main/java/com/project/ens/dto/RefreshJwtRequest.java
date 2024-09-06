package com.project.ens.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshJwtRequest {
    private String refreshToken;
}
