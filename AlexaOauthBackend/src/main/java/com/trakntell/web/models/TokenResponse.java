package com.trakntell.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TokenResponse {
    @JsonProperty("access_token")
    private String token;

    @JsonProperty("token_type")
    private String type;

    @JsonProperty("expires_in")
    private int expires_in;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public TokenResponse(String token, String type, int expires_in) {
        this.token = token;
        this.type = type;
        this.expires_in = expires_in;
    }
}
