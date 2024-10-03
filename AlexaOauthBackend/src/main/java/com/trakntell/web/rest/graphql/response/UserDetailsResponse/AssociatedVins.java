package com.trakntell.web.rest.graphql.response.UserDetailsResponse;

public class AssociatedVins {
    String vin;

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    UserType type;

}
