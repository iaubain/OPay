package com.oltranz.opay.models.signup;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/16/2017.
 */

public class SignUpUserResponse {
    private String code;
    private String description;
    private BodyModel body;

    public SignUpUserResponse() {
    }

    public SignUpUserResponse(String code, String description, BodyModel body) {

        this.setCode(code);
        this.setDescription(description);
        this.setBody(body);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BodyModel getBody() {
        return body;
    }

    public void setBody(BodyModel body) {
        this.body = body;
    }
}
