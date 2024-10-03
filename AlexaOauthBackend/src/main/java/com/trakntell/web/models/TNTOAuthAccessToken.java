package com.trakntell.web.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Blob;

@Entity
@Table(name = "oauth_access_token")
public class TNTOAuthAccessToken {

    private String token_id;

    private Blob token;

    private String authentication_id;

    @Id
    private String user_name;

    private String client_id;

    private Blob authentication;

    private String refresh_token;

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public Blob getToken() {
        return token;
    }

    public void setToken(Blob token) {
        this.token = token;
    }

    public String getAuthentication_id() {
        return authentication_id;
    }

    public void setAuthentication_id(String authentication_id) {
        this.authentication_id = authentication_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public Blob getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Blob authentication) {
        this.authentication = authentication;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    @Override
    public String toString() {
        return "TNTOAuthAccessToken{" +
                "token_id='" + token_id + '\'' +
                ", token=" + token +
                ", authentication_id='" + authentication_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", client_id='" + client_id + '\'' +
                ", authentication=" + authentication +
                ", refresh_token='" + refresh_token + '\'' +
                '}';
    }
}
