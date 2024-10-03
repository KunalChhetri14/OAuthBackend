package com.trakntell.web.rest.models.response;

public class RequestOTPResponse {

    private String orgid;
    private String vehicleid;

    public RequestOTPResponse(String orgid, String vehicleid) {
        this.orgid = orgid;
        this.vehicleid = vehicleid;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getVehicleid() {
        return vehicleid;
    }

    public void setVehicleid(String vehicleid) {
        this.vehicleid = vehicleid;
    }
}
