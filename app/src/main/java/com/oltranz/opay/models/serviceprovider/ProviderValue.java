package com.oltranz.opay.models.serviceprovider;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/19/2017.
 */

public class ProviderValue implements Comparable<ProviderValue> {
    private String name;
    private String value;

    public ProviderValue() {
    }

    public ProviderValue(String name, String value) {

        this.setName(name);
        this.setValue(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(ProviderValue o) {
        return value.compareTo(o.value);
    }
}
