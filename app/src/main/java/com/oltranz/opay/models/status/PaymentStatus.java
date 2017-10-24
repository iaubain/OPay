package com.oltranz.opay.models.status;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/12/2017.
 */

public class PaymentStatus {
    private String code;
    private String description;
    private PaymentData body;

    public PaymentStatus() {
    }

    public PaymentStatus(String code, String description, PaymentData body) {

        this.setCode(code);
        this.setDescription(description);
        this.setBody(body);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PaymentData getBody() {
        return body;
    }

    public void setBody(PaymentData body) {
        this.body = body;
    }
}
