package com.example.test.dto.responses;

import lombok.Data;

@Data
public class SignUpResponseDto {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String displayName;
}
