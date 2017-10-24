package com.oltranz.opay.models.wallet;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/10/2017.
 */

public class WalletType {
    private String description;
    private int id;
    private boolean isActive;
    private String name;

    public WalletType() {
    }

    public WalletType(String description, int id, boolean isActive, String name) {

        this.setDescription(description);
        this.setId(id);
        this.setActive(isActive);
        this.setName(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
