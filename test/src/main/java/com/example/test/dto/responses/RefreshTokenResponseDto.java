package com.example.test.dto.responses;

import lombok.Data;

@Data
public class RefreshTokenResponseDto {
    private String token;
    private String refreshToken;
}
