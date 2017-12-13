package com.arcthos.smartframework.smartorm;

import android.util.Log;

import com.arcthos.smartframework.annotations.Ignore;
import com.arcthos.smartframework.annotations.SObject;
import com.arcthos.smartframework.annotations.Sync;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.salesforce.androidsdk.smartsync.model.SalesforceObject;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.UUID;

/**
 * Created by Vinicius Damiati on 05-Oct-17.
 */

public abstract class SmartObject implements Serializable, Cloneable {
    @Sync(up = false)
    @SerializedName(SmartObjectConstants.ID)
    protected String id;

    @Ignore
    @Sync(up = false, down = false)
    protected Attributes attributes;

    @Sync(up = false)
    @SerializedName(SmartObjectConstants.LAST_MODIFIED_DATE)
    protected String lastModifiedDate;

    @Sync(up = false, down = false)
    @SerializedName(SmartObjectConstants.LOCALLY_CREATE)
    protected boolean locallyCreated;

    @Sync(up = false, down = false)
    @SerializedName(SmartObjectConstants.LOCALLY_UPDATED)
    protected boolean locallyUpdated;

    @Sync(up = false, down = false)
    @SerializedName(SmartObjectConstants.LOCALLY_DELETED)
    protected boolean locallyDeleted;

    @Sync(up = false, down = false)
    @SerializedName(SmartObjectConstants.LOCAL)
    protected boolean local;

    @Ignore
    @Sync(up = false, down = false)
    @Expose(serialize = false)
    @SerializedName(SmartObjectConstants.SOUP_ENTRY_ID)
    protected int soupEntryId;

    public SmartObject(Class<?> extendedClass) {
        this.attributes = new Attributes();
        this.soupEntryId = -1;
        this.id = UUID.randomUUID().toString();

        if(!extendedClass.isAnnotationPresent(SObject.class)) {
            try {
                throw new SObjectAnnotationNotFoundException("SObject annotation missing in model class: " + extendedClass.getSimpleName());
            } catch (SObjectAnnotationNotFoundException e) {
                Log.e(SmartObject.class.getSimpleName(), e.getMessage(), e);
                this.attributes.setType("");
                return;
            }
        }

        for(Annotation annotation : extendedClass.getAnnotations()) {
            if(annotation instanceof SObject){
                this.attributes.setType(((SObject)annotation).value());
                return;
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
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

    public boolean isLocallyModified() {
        if(locallyCreated || locallyDeleted || locallyUpdated) {
            return true;
        }

        return false;
    }
}
