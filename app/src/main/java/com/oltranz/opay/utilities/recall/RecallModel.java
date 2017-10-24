package com.oltranz.opay.utilities.recall;

import com.oltranz.opay.models.payment.PaymentRequest;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/11/2017.
 */

public class RecallModel {
    private String token;
    private PaymentRequest paymentRequest;

    public RecallModel() {
    }

    public RecallModel(String token, PaymentRequest paymentRequest) {

        this.setToken(token);
        this.setPaymentRequest(paymentRequest);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    public void setPaymentRequest(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }
}
