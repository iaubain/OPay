package com.oltranz.opay.models.signup.registermerchant;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/18/2017.
 */

public class RegisterMerchantResponse {
    private String id;
    private String merchantCode;
    private String merchantName;
    private String organizationName;
    private String address;
    private String organizationEmail;
    private String website;
    private String tin;
    private ContactExchangeModel adminContact;
    private ContactExchangeModel techContact;
    private String creationTime;
    private String createdByUserId;
    private String lastUpdateByUserId;
    private String lastUpdateTime;

    public RegisterMerchantResponse() {
    }

    public RegisterMerchantResponse(String id, String merchantCode, String merchantName, String organizationName, String address, String organizationEmail, String website, String tin, ContactExchangeModel adminContact, ContactExchangeModel techContact, String creationTime, String createdByUserId, String lastUpdateByUserId, String lastUpdateTime) {

        this.setId(id);
        this.setMerchantCode(merchantCode);
        this.setMerchantName(merchantName);
        this.setOrganizationName(organizationName);
        this.setAddress(address);
        this.setOrganizationEmail(organizationEmail);
        this.setWebsite(website);
        this.setTin(tin);
        this.setAdminContact(adminContact);
        this.setTechContact(techContact);
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

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrganizationEmail() {
        return organizationEmail;
    }

    public void setOrganizationEmail(String organizationEmail) {
        this.organizationEmail = organizationEmail;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTin() {
        return tin;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }

    public ContactExchangeModel getAdminContact() {
        return adminContact;
    }

    public void setAdminContact(ContactExchangeModel adminContact) {
        this.adminContact = adminContact;
    }

    public ContactExchangeModel getTechContact() {
        return techContact;
    }

    public void setTechContact(ContactExchangeModel techContact) {
        this.techContact = techContact;
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
