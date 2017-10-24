package com.oltranz.opay.models.signup.registermerchant;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/18/2017.
 */

public class MerchantContact {
    private String name;
    private String phoneNum;
    private String email;

    public MerchantContact() {
    }

    public MerchantContact(String name, String phoneNum, String email) {

        this.setName(name);
        this.setPhoneNum(phoneNum);
        this.setEmail(email);
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
}
