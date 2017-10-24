package com.oltranz.opay.models.signup;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/17/2017.
 */

public class SignUpUserRequest {
    private String email;
    private String firstName;
    private String lastName;
    private String telephoneNumber;

    public SignUpUserRequest() {
    }

    public SignUpUserRequest(String email, String firstName, String lastName, String telephoneNumber) {

        this.setEmail(email);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setTelephoneNumber(telephoneNumber);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
