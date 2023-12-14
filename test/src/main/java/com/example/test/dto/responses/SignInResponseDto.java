package com.example.test.dto.responses;

import lombok.Data;

@Data
public class SignInResponseDto {

    private String firstName;
    private String lastName;
    private String email;
    private String displayName;
    private String token;
    private String refreshToken;
}
