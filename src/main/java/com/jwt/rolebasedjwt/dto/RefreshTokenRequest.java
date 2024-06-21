package com.jwt.rolebasedjwt.dto;

import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String token;
}
