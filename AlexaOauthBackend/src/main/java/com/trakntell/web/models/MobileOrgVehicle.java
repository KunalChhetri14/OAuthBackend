package com.trakntell.web.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mobile_orgid_vehicleid")
public class MobileOrgVehicle {

    @Id
    @Column(name = "mobile")
    private String mobileNum;

    @Column(name = "orgid")
    private String orgid;

    @Column(name = "vehicleid")
    private String vehicleid;

    @Column(name = "subscription_status")
    private Integer subscription_status;

    @Column(name = "subscription_end_date")
    private LocalDateTime subscription_end_date;

    @Column(name = "created")
    private String created;

    @Column(name = "modified")
    private String modified;

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
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

    public int getSubscription_status() {
        return subscription_status;
    }

    public void setSubscription_status(int subscription_status) {
        this.subscription_status = subscription_status;
    }

    public LocalDateTime getSubscription_end_date() {
        return subscription_end_date;
    }

    public void setSubscription_end_date(LocalDateTime subscription_end_date) {
        this.subscription_end_date = subscription_end_date;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }
}
