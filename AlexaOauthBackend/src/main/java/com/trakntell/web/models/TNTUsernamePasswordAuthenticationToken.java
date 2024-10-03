package com.trakntell.web.models;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class TNTUsernamePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private String tvsmToken;
    private String orgid;
    private String vehicleid;

    public TNTUsernamePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public String getTvsmToken() {
        return tvsmToken;
    }

    public void setTvsmToken(String tvsmToken) {
        this.tvsmToken = tvsmToken;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getVehicleid() {
        return vehicleid;
    }

    public void setVehicleid(String vehicleid) {
        this.vehicleid = vehicleid;
    }
}
