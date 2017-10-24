package com.oltranz.opay.models.signup.registermerchant;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/18/2017.
 */

public class RegisterMerchantRequest {
    private String initialUserId;
    private String merchantName;
    private String organizationName;
    private String address;
    private String organizationEmail;
    private String website;
    private String tin;
    private MerchantContact adminContact;
    private MerchantContact techContact;

    public RegisterMerchantRequest() {
    }

    public RegisterMerchantRequest(String initialUserId, String merchantName, String organizationName, String address, String organizationEmail, String website, String tin, MerchantContact adminContact, MerchantContact techContact) {

        this.setInitialUserId(initialUserId);
        this.setMerchantName(merchantName);
        this.setOrganizationName(organizationName);
        this.setAddress(address);
        this.setOrganizationEmail(organizationEmail);
        this.setWebsite(website);
        this.setTin(tin);
        this.setAdminContact(adminContact);
        this.setTechContact(techContact);
    }

    public String getInitialUserId() {
        return initialUserId;
    }

    public void setInitialUserId(String initialUserId) {
        this.initialUserId = initialUserId;
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

    public MerchantContact getAdminContact() {
        return adminContact;
    }

    public void setAdminContact(MerchantContact adminContact) {
        this.adminContact = adminContact;
    }

    public MerchantContact getTechContact() {
        return techContact;
    }

    public void setTechContact(MerchantContact techContact) {
        this.techContact = techContact;
    }
}
