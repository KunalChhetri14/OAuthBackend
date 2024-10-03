package com.trakntell.web.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SendOtpResponse {
    public static final String SUCCESS	= "Success";

    @JsonProperty("StatusCode")
    private int status;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Result")
    private String result;

    @JsonProperty("Data")
    private String data;

    public SendOtpResponse(int status, String message, String result, String data) {
        this.status = status;
        this.message = message;
        this.result = result;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
