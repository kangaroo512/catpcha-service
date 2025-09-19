package com.languageproject.v1.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String name;

    private int age;

    private String email;

    private String[] nativeLanguages;

    private String password;


}
