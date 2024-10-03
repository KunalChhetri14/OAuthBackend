package com.trakntell.web.rest.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionStatusUpdateResponse {
    public static final String STATUS_OK        = "ok";
    public static final String STATUS_ERROR     = "error";

    @JsonProperty("status")
    private String status;

    @JsonProperty("errormsg")
    private String errormsg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public SubscriptionStatusUpdateResponse(String status) {
        this.status = status;
    }

    public SubscriptionStatusUpdateResponse(String status, String errormsg) {
        this.status = status;
        this.errormsg = errormsg;
    }
}
