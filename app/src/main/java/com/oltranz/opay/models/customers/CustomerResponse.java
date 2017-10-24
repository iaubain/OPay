package com.oltranz.opay.models.customers;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/19/2017.
 */

public class CustomerResponse {
    private String newClients;
    private String returns;
    private String total;

    public CustomerResponse() {
    }

    public CustomerResponse(String newClients, String returns, String total) {

        this.setNewClients(newClients);
        this.setReturns(returns);
        this.setTotal(total);
    }

    public String getNewClients() {
        return newClients;
    }

    public void setNewClients(String newClients) {
        this.newClients = newClients;
    }

    public String getReturns() {
        return returns;
    }

    public void setReturns(String returns) {
        this.returns = returns;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
