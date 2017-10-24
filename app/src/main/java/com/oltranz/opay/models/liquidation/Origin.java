package com.oltranz.opay.models.liquidation;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/11/2017.
 */

public class Origin {
    private String application;
    private String ref_transaction;

    public Origin() {
    }

    public Origin(String application, String ref_transaction) {

        this.setApplication(application);
        this.setRef_transaction(ref_transaction);
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getRef_transaction() {
        return ref_transaction;
    }

    public void setRef_transaction(String ref_transaction) {
        this.ref_transaction = ref_transaction;
    }
}
