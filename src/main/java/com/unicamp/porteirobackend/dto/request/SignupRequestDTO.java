package com.unicamp.porteirobackend.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class SignupRequestDTO {
    private String username;
    private String email;
    private String password;
    private Set<String> roles;
}
