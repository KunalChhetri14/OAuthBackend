package com.trakntell.web.rest.models.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TNTOTPResponse {
    public static final String STATUS_OK        = "ok";
    public static final String STATUS_ERROR     = "error";

    @JsonProperty("status")
    private String status;

    @JsonProperty("orgid")
    private String orgid;

    @JsonProperty("vehicleid")
    private String vehicleid;


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
