package com.example.test.dto.requests;

import lombok.Data;

@Data
public class SignUpRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}