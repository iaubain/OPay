package com.oltranz.opay.models.payments;

import java.util.List;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/9/2017.
 */

public class ServiceProvider {
    private String spId;
    private String logoUri;
    private String providerName;
    private List<PaymentMode> paymentModes;
    public ServiceProvider() {

    }
    public ServiceProvider(String spId, String logoUri, String providerName) {
        this.setSpId(spId);
        this.setLogoUri(logoUri);
        this.setProviderName(providerName);
    }

    public ServiceProvider(String spId, String logoUri, String providerName, List<PaymentMode> paymentModes) {
        this.spId = spId;
        this.logoUri = logoUri;
        this.providerName = providerName;
        this.setPaymentModes(paymentModes);
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public List<PaymentMode> getPaymentModes() {
        return paymentModes;
    }

    public void setPaymentModes(List<PaymentMode> paymentModes) {
        this.paymentModes = paymentModes;
    }
}
