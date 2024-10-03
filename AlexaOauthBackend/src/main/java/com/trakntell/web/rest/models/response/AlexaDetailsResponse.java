package com.trakntell.web.rest.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class AlexaDetailsResponse {
    public static final String SUCCESS	= "Success";

    @JsonProperty("StatusCode")
    private int status;

    @JsonProperty("Message")
    private String message;

    @JsonProperty("Result")
    private String result;

    @JsonProperty("Data")
    private ArrayList<Details> data;

    @JsonProperty("ErrorMessages")
    private String errorMessages;

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

    public ArrayList<Details> getData() {
        return data;
    }

    public void setData(ArrayList<Details> data) {
        this.data = data;
    }

    public String getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(String errorMessages) {
        this.errorMessages = errorMessages;
    }

    public static class Details {
        @JsonProperty("UserId")
        private String userid;

        @JsonProperty("MobileNumber")
        private String mobile;

        @JsonProperty("VehicleTypeId")
        private int vehicleTypeId;

        @JsonProperty("IsAlexaEnabled")
        private boolean isAlexaEnabled;

        @JsonProperty("SubscriptionEndDate")
        private String subscriptionEndDate;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getVehicleTypeId() {
            return vehicleTypeId;
        }

        public void setVehicleTypeId(int vehicleTypeId) {
            this.vehicleTypeId = vehicleTypeId;
        }

        public boolean isAlexaEnabled() {
            return isAlexaEnabled;
        }

        public void setAlexaEnabled(boolean alexaEnabled) {
            isAlexaEnabled = alexaEnabled;
        }

        public String getSubscriptionEndDate() {
            return subscriptionEndDate;
        }

        public void setSubscriptionEndDate(String subscriptionEndDate) {
            this.subscriptionEndDate = subscriptionEndDate;
        }
    }
}
