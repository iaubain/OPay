package com.oltranz.opay.models.user;

import java.util.List;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/10/2017.
 */

public class UserDetails {
    private User user;
    private String accessToken;
    private List<Permissions> permissions;

    public UserDetails() {

    }

    public UserDetails(User user, String accessToken, List<Permissions> permissions) {
        this.setUser(user);
        this.setAccessToken(accessToken);
        this.setPermissions(permissions);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }
}
