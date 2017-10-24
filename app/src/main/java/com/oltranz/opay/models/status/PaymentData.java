package com.oltranz.opay.models.status;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/12/2017.
 */

public class PaymentData {
    private String id;
    private String merchantId;
    private String merchantRequestRef;
    private String requestorUserId;
    private String amount;
    private String status;
    private String requestDescr;
    private String requestTime;
    private String lastStatusTime;
    private String payingSpId;
    private String statusDescr;
    private String payingAcoountId;
    private String paymentType;
    private String initSystemId;
    private String paymentRef;

    public PaymentData() {
    }

    public PaymentData(String id, String merchantId, String merchantRequestRef, String requestorUserId, String amount, String status, String requestDescr, String requestTime, String lastStatusTime, String payingSpId, String statusDescr, String payingAcoountId, String paymentType, String initSystemId, String paymentRef) {

        this.setId(id);
        this.setMerchantId(merchantId);
        this.setMerchantRequestRef(merchantRequestRef);
        this.setRequestorUserId(requestorUserId);
        this.setAmount(amount);
        this.setStatus(status);
        this.setRequestDescr(requestDescr);
        this.setRequestTime(requestTime);
        this.setLastStatusTime(lastStatusTime);
        this.setPayingSpId(payingSpId);
        this.setStatusDescr(statusDescr);
        this.setPayingAcoountId(payingAcoountId);
        this.setPaymentType(paymentType);
        this.setInitSystemId(initSystemId);
        this.setPaymentRef(paymentRef);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantRequestRef() {
        return merchantRequestRef;
    }

    public void setMerchantRequestRef(String merchantRequestRef) {
        this.merchantRequestRef = merchantRequestRef;
    }

    public String getRequestorUserId() {
        return requestorUserId;
    }

    public void setRequestorUserId(String requestorUserId) {
        this.requestorUserId = requestorUserId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRequestDescr() {
        return requestDescr;
    }

    public void setRequestDescr(String requestDescr) {
        this.requestDescr = requestDescr;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getLastStatusTime() {
        return lastStatusTime;
    }

    public void setLastStatusTime(String lastStatusTime) {
        this.lastStatusTime = lastStatusTime;
    }

    public String getPayingSpId() {
        return payingSpId;
    }

    public void setPayingSpId(String payingSpId) {
        this.payingSpId = payingSpId;
    }

    public String getStatusDescr() {
        return statusDescr;
    }

    public void setStatusDescr(String statusDescr) {
        this.statusDescr = statusDescr;
    }

    public String getPayingAcoountId() {
        return payingAcoountId;
    }

    public void setPayingAcoountId(String payingAcoountId) {
        this.payingAcoountId = payingAcoountId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getInitSystemId() {
        return initSystemId;
    }

    public void setInitSystemId(String initSystemId) {
        this.initSystemId = initSystemId;
    }

    public String getPaymentRef() {
        return paymentRef;
    }

    public void setPaymentRef(String paymentRef) {
        this.paymentRef = paymentRef;
    }
}
