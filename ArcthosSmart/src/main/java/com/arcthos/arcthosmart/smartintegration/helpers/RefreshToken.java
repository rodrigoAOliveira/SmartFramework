package com.arcthos.arcthosmart.smartintegration.helpers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vinicius Damiati on 16-Oct-17.
 */

public class RefreshToken {
    private String id;

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("instance_url")
    private String instanceUrl;

    @SerializedName("issued_at")
    private String issuedAt;

    private String signature;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getInstanceUrl() {
        return instanceUrl;
    }

    public void setInstanceUrl(String instanceUrl) {
        this.instanceUrl = instanceUrl;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
