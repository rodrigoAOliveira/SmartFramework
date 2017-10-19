package com.arcthos.smartframework.smartintegration.helpers;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

/**
 * Created by Vinicius Damiati on 19-Oct-17.
 */

public class RefreshTokenRequest {
    @SerializedName("grant_type")
    private String grantType;

    @SerializedName("client_id")
    private String clientId;

    @SerializedName("refresh_token")
    private String refreshToken;

    public RefreshTokenRequest() {}

    public RefreshTokenRequest(String grantType, String clientId, String refreshToken) {
        this.grantType = grantType;
        this.clientId = clientId;
        this.refreshToken = refreshToken;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
