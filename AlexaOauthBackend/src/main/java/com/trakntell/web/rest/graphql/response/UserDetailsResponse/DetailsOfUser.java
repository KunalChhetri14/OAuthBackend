package com.trakntell.web.rest.graphql.response.UserDetailsResponse;

import java.util.List;

public class DetailsOfUser {

    private String app_user_id;

    private String user_name;

    private String email;

    private String iso_code;

    private int country_code;

    private String mobile_number;

    private AssociatedVins[] associated_vins;

    private boolean isFCM;

    public AssociatedVins[] getAssociated_vins() {
        return associated_vins;
    }

    public void setAssociated_vins(AssociatedVins[] associated_vins) {
        this.associated_vins = associated_vins;
    }

    public String getApp_user_id() {
        return app_user_id;
    }

    public void setApp_user_id(String app_user_id) {
        this.app_user_id = app_user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIso_code() {
        return iso_code;
    }

    public void setIso_code(String iso_code) {
        this.iso_code = iso_code;
    }

    public int getCountry_code() {
        return country_code;
    }

    public void setCountry_code(int country_code) {
        this.country_code = country_code;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public boolean isFCM() {
        return isFCM;
    }

    public void setFCM(boolean FCM) {
        isFCM = FCM;
    }
}
