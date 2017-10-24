package com.oltranz.opay.models.signup.registermerchant;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/18/2017.
 */
public class ContactExchangeModel {
    private String id;
    private String name;
    private String phoneNum;
    private String email;
    private String creationTime;
    private String createdByUserId;
    private String lastUpdateByUserId;
    private String lastUpdateTime;

    public ContactExchangeModel() {
    }

    public ContactExchangeModel(String id, String name, String phoneNum, String email, String creationTime, String createdByUserId, String lastUpdateByUserId, String lastUpdateTime) {

        this.setId(id);
        this.setName(name);
        this.setPhoneNum(phoneNum);
        this.setEmail(email);
        this.setCreationTime(creationTime);
        this.setCreatedByUserId(createdByUserId);
        this.setLastUpdateByUserId(lastUpdateByUserId);
        this.setLastUpdateTime(lastUpdateTime);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getLastUpdateByUserId() {
        return lastUpdateByUserId;
    }

    public void setLastUpdateByUserId(String lastUpdateByUserId) {
        this.lastUpdateByUserId = lastUpdateByUserId;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
