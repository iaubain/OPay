package com.oltranz.opay.models.wallet;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/10/2017.
 */

public class Balance {
    private String commission;
    private String dueAmount;
    private String totalAmount;

    public Balance() {
    }

    public Balance(String commission, String dueAmount, String totalAmount) {
        this.setCommission(commission);
        this.setDueAmount(dueAmount);
        this.setTotalAmount(totalAmount);
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(String dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
}
