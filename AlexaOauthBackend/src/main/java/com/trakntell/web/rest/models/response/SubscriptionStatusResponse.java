package com.trakntell.web.rest.models.response;

public class SubscriptionStatusResponse {

    public static final String CODE_INVALID_MOBILE_NUMBER   = "INVALID_MOBILE_NUMBER";
    public static final String CODE_INVALID_DATE            = "INVALID_DATE";
    public static final String CODE_SUBSCRIPTION_UPDATED    = "SUBSCRIPTION_UPDATED";
    public static final String CODE_USER_NOT_SIGNED_UP      = "USER_NOT_SIGNED_UP";
    public static final String CODE_SERVER_ERROR            = "INTERNAL_SERVER_ERROR";
    public static final String CODE_MISSING_FIELDS          = "MISSING_FIELDS";

    private String status;
    private String code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
