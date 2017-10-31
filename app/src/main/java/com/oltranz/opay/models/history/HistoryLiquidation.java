package com.oltranz.opay.models.history;

import com.oltranz.opay.models.serviceprovider.ProviderValue;

import java.util.HashMap;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/25/2017.
 */

public class HistoryLiquidation{
    private String amount;
    private String commission;
    private String date;
    private boolean isCompleted;
    private boolean isLiquidated;
    private String merchantEmail;
    private String merchantName;
    private String merchantTelephone;
    private String transactionId;
    private String transaction_entity;
    private String transaction_entity_ref;
    private String type;
    private String userId;
    private static HashMap<String, String> mMap = new HashMap<>();

    public HistoryLiquidation() {
    }

    public HistoryLiquidation(String amount, String commission, String date, boolean isCompleted, boolean isLiquidated, String merchantEmail, String merchantName, String merchantTelephone, String transactionId, String transaction_entity, String transaction_entity_ref, String type, String userId) {

        this.setAmount(amount);
        this.setCommission(commission);
        this.setDate(date);
        this.setCompleted(isCompleted);
        this.setLiquidated(isLiquidated);
        this.setMerchantEmail(merchantEmail);
        this.setMerchantName(merchantName);
        this.setMerchantTelephone(merchantTelephone);
        this.setTransactionId(transactionId);
        this.setTransaction_entity(transaction_entity);
        this.setTransaction_entity_ref(transaction_entity_ref);
        this.setType(type);
        this.setUserId(userId);
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isLiquidated() {
        return isLiquidated;
    }

    public void setLiquidated(boolean liquidated) {
        isLiquidated = liquidated;
    }

    public String getMerchantEmail() {
        return merchantEmail;
    }

    public void setMerchantEmail(String merchantEmail) {
        this.merchantEmail = merchantEmail;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantTelephone() {
        return merchantTelephone;
    }

    public void setMerchantTelephone(String merchantTelephone) {
        this.merchantTelephone = merchantTelephone;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransaction_entity() {
        return transaction_entity;
    }

    public void setTransaction_entity(String transaction_entity) {
        this.transaction_entity = transaction_entity;
    }

    public String getTransaction_entity_ref() {
        return transaction_entity_ref;
    }

    public void setTransaction_entity_ref(String transaction_entity_ref) {
        this.transaction_entity_ref = transaction_entity_ref;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "HistoryLiquidation{" +
                "amount='" + amount + '\'' +
                ", commission='" + commission + '\'' +
                ", date='" + date + '\'' +
                ", isCompleted=" + isCompleted +
                ", isLiquidated=" + isLiquidated +
                ", merchantEmail='" + merchantEmail + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", merchantTelephone='" + merchantTelephone + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", transaction_entity='" + transaction_entity + '\'' +
                ", transaction_entity_ref='" + transaction_entity_ref + '\'' +
                ", type='" + type + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public HashMap<String, String> objectMap() {
        mMap.clear();
        if (transactionId != null)
            mMap.put("TX_ID", transactionId);
        if (amount != null)
            mMap.put("Amount", amount);
        if (commission != null)
            mMap.put("Commission", commission);
        if (date != null)
            mMap.put("Date", date);
        if (merchantEmail != null)
            mMap.put("Email", merchantEmail);
        if (merchantName != null)
            mMap.put("Name", merchantName);
        if (merchantTelephone != null)
            mMap.put("Telephone", merchantTelephone);
        if (transaction_entity != null)
            mMap.put("Entity", transaction_entity);
        if (transaction_entity_ref != null)
            mMap.put("Reference", transaction_entity_ref);
        if (type != null)
            mMap.put("Type", type);

        mMap.put("Liquidated", isLiquidated ? "Yes" : "No");
        mMap.put("Completed", isCompleted ? "Yes" : "No");
        return mMap;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof HistoryLiquidation)) return false;

        HistoryLiquidation that = (HistoryLiquidation) object;

        if (isCompleted() != that.isCompleted()) return false;
        if (isLiquidated() != that.isLiquidated()) return false;
        if (getAmount() != null ? !getAmount().equals(that.getAmount()) : that.getAmount() != null)
            return false;
        if (getCommission() != null ? !getCommission().equals(that.getCommission()) : that.getCommission() != null)
            return false;
        if (getDate() != null ? !getDate().equals(that.getDate()) : that.getDate() != null)
            return false;
        if (getMerchantEmail() != null ? !getMerchantEmail().equals(that.getMerchantEmail()) : that.getMerchantEmail() != null)
            return false;
        if (getMerchantName() != null ? !getMerchantName().equals(that.getMerchantName()) : that.getMerchantName() != null)
            return false;
        if (getMerchantTelephone() != null ? !getMerchantTelephone().equals(that.getMerchantTelephone()) : that.getMerchantTelephone() != null)
            return false;
        if (getTransactionId() != null ? !getTransactionId().equals(that.getTransactionId()) : that.getTransactionId() != null)
            return false;
        if (getTransaction_entity() != null ? !getTransaction_entity().equals(that.getTransaction_entity()) : that.getTransaction_entity() != null)
            return false;
        if (getTransaction_entity_ref() != null ? !getTransaction_entity_ref().equals(that.getTransaction_entity_ref()) : that.getTransaction_entity_ref() != null)
            return false;
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null)
            return false;
        return getUserId() != null ? getUserId().equals(that.getUserId()) : that.getUserId() == null;

    }

    @Override
    public int hashCode() {
        int result = getAmount() != null ? getAmount().hashCode() : 0;
        result = 31 * result + (getCommission() != null ? getCommission().hashCode() : 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (isCompleted() ? 1 : 0);
        result = 31 * result + (isLiquidated() ? 1 : 0);
        result = 31 * result + (getMerchantEmail() != null ? getMerchantEmail().hashCode() : 0);
        result = 31 * result + (getMerchantName() != null ? getMerchantName().hashCode() : 0);
        result = 31 * result + (getMerchantTelephone() != null ? getMerchantTelephone().hashCode() : 0);
        result = 31 * result + (getTransactionId() != null ? getTransactionId().hashCode() : 0);
        result = 31 * result + (getTransaction_entity() != null ? getTransaction_entity().hashCode() : 0);
        result = 31 * result + (getTransaction_entity_ref() != null ? getTransaction_entity_ref().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getUserId() != null ? getUserId().hashCode() : 0);
        return result;
    }
}
