package com.trakntell.web.rest.models.request;

public class OTPRequest {
    private String mobileNumber;

    public OTPRequest(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
