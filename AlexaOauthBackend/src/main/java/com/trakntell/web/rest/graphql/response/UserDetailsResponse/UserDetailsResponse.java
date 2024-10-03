package com.trakntell.web.rest.graphql.response.UserDetailsResponse;

public class UserDetailsResponse {

    public UserDetailsObject getDetails() {
        return details;
    }

    public void setDetails(UserDetailsObject details) {
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    private UserDetailsObject details;
    private int status;
    private String statusMessage;

}
