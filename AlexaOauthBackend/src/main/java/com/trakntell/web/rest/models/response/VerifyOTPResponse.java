package com.trakntell.web.rest.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class VerifyOTPResponse {
    @JsonProperty("StatusCode")
    private int StatusCode;

    @JsonProperty("Message")
    private String Message;

    @JsonProperty("Result")
    private String Result;

    @JsonProperty("Data")
    private List<UserData> Data;

    public VerifyOTPResponse() {}

    public static class UserData {

        private String UserId;
        private String FullName;
        private String MobileNumber;
        private String Email;
        private int CityId;
        private String CityName;
        private String BloodGroup;
        private String AllergicContent;
        private String ProfileImagePath;

        @JsonProperty("Token")
        private String Token;

        private String FacebookId;
        private String GooglePlusId;
        private String CrashAlertDate;
        private String Addedvehiclecount;

        public String getUserId() {
            return UserId;
        }

        public void setUserId(String userId) {
            UserId = userId;
        }

        public String getFullName() {
            return FullName;
        }

        public void setFullName(String fullName) {
            FullName = fullName;
        }

        public String getMobileNumber() {
            return MobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            MobileNumber = mobileNumber;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public int getCityId() {
            return CityId;
        }

        public void setCityId(int cityId) {
            CityId = cityId;
        }

        public String getCityName() {
            return CityName;
        }

        public void setCityName(String cityName) {
            CityName = cityName;
        }

        public String getBloodGroup() {
            return BloodGroup;
        }

        public void setBloodGroup(String bloodGroup) {
            BloodGroup = bloodGroup;
        }

        public String getAllergicContent() {
            return AllergicContent;
        }

        public void setAllergicContent(String allergicContent) {
            AllergicContent = allergicContent;
        }

        public String getProfileImagePath() {
            return ProfileImagePath;
        }

        public void setProfileImagePath(String profileImagePath) {
            ProfileImagePath = profileImagePath;
        }

        public String getToken() {
            return Token;
        }

        public void setToken(String token) {
            Token = token;
        }

        public String getFacebookId() {
            return FacebookId;
        }

        public void setFacebookId(String facebookId) {
            FacebookId = facebookId;
        }

        public String getGooglePlusId() {
            return GooglePlusId;
        }

        public void setGooglePlusId(String googlePlusId) {
            GooglePlusId = googlePlusId;
        }

        public String getCrashAlertDate() {
            return CrashAlertDate;
        }

        public void setCrashAlertDate(String crashAlertDate) {
            CrashAlertDate = crashAlertDate;
        }

        public String getAddedvehiclecount() {
            return Addedvehiclecount;
        }

        public void setAddedvehiclecount(String addedvehiclecount) {
            Addedvehiclecount = addedvehiclecount;
        }
    }

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public List<UserData> getData() {
        return Data;
    }

    public void setData(List<UserData> data) {
        Data = data;
    }
}

