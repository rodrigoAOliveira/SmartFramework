package com.arcthos.smartframework.smartorm;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Vinicius Damiati on 05-Oct-17.
 */

public abstract class SmartObject implements Serializable {
    @SerializedName("Id")
    protected String id;

    @SerializedName("Name")
    protected String name;

    protected Attributes attributes;

    @SerializedName("IsDeleted")
    protected boolean deleted;

    @SerializedName("LastModifiedDate")
    protected String lastModifiedDate;

    @SerializedName("__locally_created__")
    protected boolean locallyCreated;

    @SerializedName("__locally_updated__")
    protected boolean locallyUpdated;

    @SerializedName("__locally_deleted__")
    protected boolean locallyDeleted;

    @SerializedName("__local__")
    protected boolean local;

    @Expose(serialize = false)
    @SerializedName("_soupEntryId")
    protected int soupEntryId;

    public SmartObject(String sObjectName) {
        this.attributes = new Attributes();
        this.attributes.setType(sObjectName);
        this.soupEntryId = -1;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public boolean isLocallyCreated() {
        return locallyCreated;
    }

    public void setLocallyCreated(boolean locallyCreated) {
        this.locallyCreated = locallyCreated;
    }

    public boolean isLocallyUpdated() {
        return locallyUpdated;
    }

    public void setLocallyUpdated(boolean locallyUpdated) {
        this.locallyUpdated = locallyUpdated;
    }

    public boolean isLocallyDeleted() {
        return locallyDeleted;
    }

    public void setLocallyDeleted(boolean locallyDeleted) {
        this.locallyDeleted = locallyDeleted;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public int getSoupEntryId() {
        return soupEntryId;
    }

    public void setSoupEntryId(int soupEntryId) {
        this.soupEntryId = soupEntryId;
    }
}
