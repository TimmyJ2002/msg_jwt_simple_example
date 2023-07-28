package de.msg.training.jwtsimpleexample.controller.auth;

public class RefreshTokenResponse {

    private String renewedAccesToken;

    public RefreshTokenResponse(String renewedAccesToken) {
        this.renewedAccesToken = renewedAccesToken;
    }

    public String getRenewedAccesToken() {
        return renewedAccesToken;
    }

    public void setRenewedAccesToken(String renewedAccesToken) {
        this.renewedAccesToken = renewedAccesToken;
    }
}
