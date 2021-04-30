package com.arcthos.arcthosmart.model.syncerror;

import com.arcthos.arcthosmart.annotations.Sync;
import com.arcthos.arcthosmart.smartorm.GeneralConstants;
import com.arcthos.arcthosmart.smartorm.SmartObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ************************************************************
 * Autor : Cesar Augusto dos Santos
 * Data : 03/12/2018
 * Empresa : TOPi
 * ************************************************************
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class SyncErrorLog extends SmartObject {

    @Sync(up = false, down = false)
    private String dateTime;

    @Sync(up = false, down = false)
    private String request;

    @Sync(up = false, down = false)
    private String response;

    @Sync(up = false, down = false)
    private String errorType;

    @Sync(up = false, down = false)
    private String operation;

    public SyncErrorLog(Class<?> extendedClass) {
        super(extendedClass);
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
