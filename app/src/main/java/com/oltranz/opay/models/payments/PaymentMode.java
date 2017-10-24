package com.oltranz.opay.models.payments;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/9/2017.
 */

public class PaymentMode {
    private String paymentId;
    private String paymentName;
    private String paymentLogoUri;
    public PaymentMode() {
    }
    public PaymentMode(String paymentId, String paymentName, String paymentLogoUri) {
        this.setPaymentId(paymentId);
        this.setPaymentName(paymentName);
        this.setPaymentLogoUri(paymentLogoUri);
    }


    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPaymentLogoUri() {
        return paymentLogoUri;
    }

    public void setPaymentLogoUri(String paymentLogoUri) {
        this.paymentLogoUri = paymentLogoUri;
    }
}
