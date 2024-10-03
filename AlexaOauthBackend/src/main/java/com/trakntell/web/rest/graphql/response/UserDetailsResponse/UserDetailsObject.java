package com.trakntell.web.rest.graphql.response.UserDetailsResponse;

public class UserDetailsObject {

    public DetailsOfUser[] getUser_details() {
        return user_details;
    }

    public void setUser_details(DetailsOfUser[] user_details) {
        this.user_details = user_details;
    }

    private DetailsOfUser[] user_details;

}
