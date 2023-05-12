package com.unicamp.porteirobackend.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String accessToken;
    private Integer id;
    private String username;
    private String email;
    private List<String> roles;
    private String tokenType;


    public JwtResponse(String accessToken, Integer id, String username, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.tokenType = "Bearer";
    }
}
