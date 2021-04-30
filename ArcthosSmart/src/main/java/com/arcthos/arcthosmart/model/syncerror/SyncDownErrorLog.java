package com.arcthos.arcthosmart.model.syncerror;

import com.arcthos.arcthosmart.annotations.SObject;
import com.arcthos.arcthosmart.annotations.Sync;
import com.arcthos.arcthosmart.smartorm.GeneralConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@SObject(SyncDownErrorLogConstants.SYNC_DOWN_ERROR_LOG)
public class SyncDownErrorLog extends SyncErrorLog {

    @Sync(up = false, down = false)
    private String query;

    @Sync(up = false, down = false)
    private String customErrorMessage;

    @Sync(up = false, down = false)
    @JsonProperty(GeneralConstants.IS_DELETED)
    private String isDeleted;

    public SyncDownErrorLog() {
        super(SyncDownErrorLog.class);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getCustomErrorMessage() {
        return customErrorMessage;
    }

    public void setCustomErrorMessage(String customErrorMessage) {
        this.customErrorMessage = customErrorMessage;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
