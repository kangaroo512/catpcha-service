package com.languageproject.v1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserSignUpDTO {

    @NotBlank
    @Email
    private String email;

}
