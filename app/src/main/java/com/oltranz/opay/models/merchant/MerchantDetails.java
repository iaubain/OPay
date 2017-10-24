/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.oltranz.opay.models.merchant;
/**
 *
 * @author manzi
 */
public class MerchantDetails {
    private String id;
    private String merchantCode;
    private String merchantName;
    private String organizationName;
    private String address;
    private String organizationEmail;
    private String website;
    private String tin;
    private ContactExchange adminContact;
    private ContactExchange techContact;
    private String creationTime;
    private String createdByUserId;
    private String lastUpdateByUserId;
    private String lastUpdateTime;
    
    
    /**
     * @return the merchantCode
     */
    public String getMerchantCode() {
        return merchantCode;
    }
    
    /**
     * @param merchantCode the merchantCode to set
     */
    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }
    
    /**
     * @return the organizationName
     */
    public String getOrganizationName() {
        return organizationName;
    }
    
    /**
     * @param organizationName the organizationName to set
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
    
    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    /**
     * @return the organizationEmail
     */
    public String getOrganizationEmail() {
        return organizationEmail;
    }
    
    /**
     * @param organizationEmail the organizationEmail to set
     */
    public void setOrganizationEmail(String organizationEmail) {
        this.organizationEmail = organizationEmail;
    }
    
    /**
     * @return the website
     */
    public String getWebsite() {
        return website;
    }
    
    /**
     * @param website the website to set
     */
    public void setWebsite(String website) {
        this.website = website;
    }
    
    /**
     * @return the tin
     */
    public String getTin() {
        return tin;
    }
    
    /**
     * @param tin the tin to set
     */
    public void setTin(String tin) {
        this.tin = tin;
    }
    
   
    /**
     * @return the merchantName
     */
    public String getMerchantName() {
        return merchantName;
    }
    
    /**
     * @param merchantName the merchantName to set
     */
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    /**
     * @return the creationTime
     */
    public String getCreationTime() {
        return creationTime;
    }

    /**
     * @param creationTime the creationTime to set
     */
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * @return the createdByUserId
     */
    public String getCreatedByUserId() {
        return createdByUserId;
    }

    /**
     * @param createdByUserId the createdByUserId to set
     */
    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    /**
     * @return the lastUpdateByUserId
     */
    public String getLastUpdateByUserId() {
        return lastUpdateByUserId;
    }

    /**
     * @param lastUpdateByUserId the lastUpdateByUserId to set
     */
    public void setLastUpdateByUserId(String lastUpdateByUserId) {
        this.lastUpdateByUserId = lastUpdateByUserId;
    }

    /**
     * @return the lastUpdateTime
     */
    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    /**
     * @param lastUpdateTime the lastUpdateTime to set
     */
    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /**
     * @return the adminContact
     */
    public ContactExchange getAdminContact() {
        return adminContact;
    }

    /**
     * @param adminContact the adminContact to set
     */
    public void setAdminContact(ContactExchange adminContact) {
        this.adminContact = adminContact;
    }

    /**
     * @return the techContact
     */
    public ContactExchange getTechContact() {
        return techContact;
    }

    /**
     * @param techContact the techContact to set
     */
    public void setTechContact(ContactExchange techContact) {
        this.techContact = techContact;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
    
    
}
