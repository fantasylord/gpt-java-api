package com.lord.local.gptjavaapi.model;

import lombok.Data;

@Data
public class AuthResponse {
    private String tokenType;
    private String accessToken;
    private long expiresIn;
    private String scope;
    private String username;

    public AuthResponse(String tokenType, String accessToken, long expiresIn, String scope, String username) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.username = username;
    }

    // getters and setters
}
