package com.ben.app.authenticationanduser;


import lombok.Data;


@Data
public class RegisterRequestDTO {
    private String name;
    private String email;
    private String password;
}