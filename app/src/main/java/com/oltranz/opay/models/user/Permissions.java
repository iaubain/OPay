package com.oltranz.opay.models.user;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/10/2017.
 */

public class Permissions {
    private int objectId;
    private String objectName;
    private int rights;

    public Permissions() {
    }

    public Permissions(int objectId, String objectName, int rights) {
        this.setObjectId(objectId);
        this.setObjectName(objectName);
        this.setRights(rights);
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public int getRights() {
        return rights;
    }

    public void setRights(int rights) {
        this.rights = rights;
    }
}
