package com.oltranz.opay.models.liquidation;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/11/2017.
 */

public class LiquidationRequest {
    private Origin origin;
    private LiquidationWallet wallet;
    private String operation = "1";
    private String transaction_entity_name = "UNKNOWN";
    private String transaction_entity_ref = "000";
    private String transaction_entity_type = "UNKNOWN";
    private String userId;

    public LiquidationRequest() {
    }

    public LiquidationRequest(Origin origin, LiquidationWallet wallet, String userId) {
        this.setOrigin(origin);
        this.setWallet(wallet);
        this.setUserId(userId);
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public LiquidationWallet getWallet() {
        return wallet;
    }

    public void setWallet(LiquidationWallet wallet) {
        this.wallet = wallet;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getTransaction_entity_name() {
        return transaction_entity_name;
    }

    public void setTransaction_entity_name(String transaction_entity_name) {
        this.transaction_entity_name = transaction_entity_name;
    }

    public String getTransaction_entity_ref() {
        return transaction_entity_ref;
    }

    public void setTransaction_entity_ref(String transaction_entity_ref) {
        this.transaction_entity_ref = transaction_entity_ref;
    }

    public String getTransaction_entity_type() {
        return transaction_entity_type;
    }

    public void setTransaction_entity_type(String transaction_entity_type) {
        this.transaction_entity_type = transaction_entity_type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
