package com.lord.local.gptjavaapi.model.resultful;

import lombok.Data;

@Data
public class AuthResponse {
    private String tokenType;
    private String accessToken;
    private long expiresIn;
    private String scope;
    private String username;

    private Long userId;

    public AuthResponse(String tokenType, String accessToken, long expiresIn, String scope, String username,Long userId) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.scope = scope;
        this.username = username;
        this.userId=userId;
    }

    // getters and setters
}
