package com.example.test.dto.requests;

import lombok.Data;

@Data
public class SignInRequestDto {
    private String email;
    private String password;
}