package com.oltranz.opay.models.liquidation;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/11/2017.
 */

public class LiquidationWallet {
    private String amount;
    private String message;
    private String owner;//merchant ID
    private String transaction_type = "LIQUIDATION";
    private String type = "PAYMENT";

    public LiquidationWallet() {
    }

    public LiquidationWallet(String amount, String message, String owner) {
        this.setAmount(amount);
        this.setMessage(message);
        this.setOwner(owner);
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
