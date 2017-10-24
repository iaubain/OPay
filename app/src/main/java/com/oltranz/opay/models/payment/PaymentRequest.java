package com.oltranz.opay.models.payment;

import com.oltranz.opay.config.Config;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/11/2017.
 */

public class PaymentRequest {
    private String amount;
    private String payingAccountId;
    private String payingSpId;
    private String requestRef;
    private String initSystemId = Config.APP_ID;
    private String paymentType = "MOBILE";
    private String descr = "normal payment";

    public PaymentRequest() {
    }

    public PaymentRequest(String amount, String payingAccountId, String payingSpId, String requestRef) {

        this.setAmount(amount);
        this.setPayingAccountId(payingAccountId);
        this.setPayingSpId(payingSpId);
        this.setRequestRef(requestRef);
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayingAccountId() {
        return payingAccountId;
    }

    public void setPayingAccountId(String payingAccountId) {
        this.payingAccountId = payingAccountId;
    }

    public String getPayingSpId() {
        return payingSpId;
    }

    public void setPayingSpId(String payingSpId) {
        this.payingSpId = payingSpId;
    }

    public String getRequestRef() {
        return requestRef;
    }

    public void setRequestRef(String requestRef) {
        this.requestRef = requestRef;
    }

    public String getInitSystemId() {
        return initSystemId;
    }

    public void setInitSystemId(String initSystemId) {
        this.initSystemId = initSystemId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
