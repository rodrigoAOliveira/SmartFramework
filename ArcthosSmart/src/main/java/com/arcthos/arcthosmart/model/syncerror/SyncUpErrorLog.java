package com.arcthos.arcthosmart.model.syncerror;

import com.arcthos.arcthosmart.annotations.SObject;
import com.arcthos.arcthosmart.annotations.Sync;
import com.arcthos.arcthosmart.smartorm.GeneralConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@SObject(SyncUpErrorLogConstants.SYNC_UP_ERROR_LOG)
public class SyncUpErrorLog extends SyncErrorLog {

    @Sync(up = false, down = false)
    private String fields;

    @Sync(up = false, down = false)
    @JsonProperty(GeneralConstants.IS_DELETED)
    private String isDeleted;

    public SyncUpErrorLog() {
        super(SyncUpErrorLog.class);
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
