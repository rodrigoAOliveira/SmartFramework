package com.arcthos.arcthosmart.shared.user.model;

import com.arcthos.arcthosmart.annotations.SObject;
import com.arcthos.arcthosmart.annotations.Sync;
import com.arcthos.arcthosmart.smartorm.SmartObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import com.arcthos.arcthosmart.helper.StringHelper;
import com.arcthos.arcthosmart.shared.user.model.constants.UserConstants;

@JsonIgnoreProperties(ignoreUnknown = true)
@SObject(UserConstants.USER)
public class User extends SmartObject implements Serializable {
    @Sync(up = false)
    @JsonProperty(UserConstants.NAME)
    private String name;

    @Sync(up = false)
    @JsonProperty(UserConstants.TITLE)
    private String title;

    @Sync(up = false)
    @JsonProperty(UserConstants.CITY)
    private String city;

    @Sync(up = false)
    @JsonProperty(UserConstants.EMAIL)
    private String email;

    @Sync(up = false)
    @JsonProperty(UserConstants.PHONE)
    private String phone;

    @Sync(up = false)
    @JsonProperty(UserConstants.FULL_PHOTO_URL)
    private String fullPhotoUrl;



    public User() {
        super(User.class);
    }

    public String getName() {
        return StringHelper.getString(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullPhotoUrl() {
        return fullPhotoUrl;
    }

    public void setFullPhotoUrl(String fullPhotoUrl) {
        this.fullPhotoUrl = fullPhotoUrl;
    }

}