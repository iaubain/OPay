package com.oltranz.opay.models.liquidation;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/11/2017.
 */

public class LiquidationResponse {
    private boolean body;
    private String code;
    private String description;

    public LiquidationResponse() {
    }

    public LiquidationResponse(boolean body, String code, String description) {

        this.setBody(body);
        this.setCode(code);
        this.setDescription(description);
    }

    public boolean isBody() {
        return body;
    }

    public void setBody(boolean body) {
        this.body = body;
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
}
