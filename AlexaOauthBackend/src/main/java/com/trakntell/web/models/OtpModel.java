package com.trakntell.web.models;

import javax.persistence.*;

@Entity
@Table(name = "tvsm_otp")
public class OtpModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "mobile_number")
    private String mobile;

    @Column(name = "otp")
    private String otp;

    @Column(name = "num_sent")
    private Integer numSent;

    @Column(name = "expire", columnDefinition = "DATETIME")
    private String expire;

    @Column(name = "used")
    private Integer used;

    @Column(name = "created", columnDefinition = "DATETIME")
    private String created;

    @Column(name = "modified", columnDefinition = "DATETIME")
    private String modified;

    /*public OtpModel(Integer id, String mobile, String otp, Integer num_sent, Date expire, Integer used, Date created, Date modified) {
        this.id = id;
        this.mobile = mobile;
        this.otp = otp;
        this.num_sent = num_sent;
        this.expire = expire;
        this.used = used;
        this.created = created;
        this.modified = modified;
    }*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Integer getNumSent() {
        return numSent;
    }

    public void setNumSent(Integer numSent) {
        this.numSent = numSent;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
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
