package com.salesforce.androidsdk.smartsync.target;

import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

/**
 * Created by Vinicius Damiati on 18-Apr-18.
 */

public class SyncDownError {
    private String query;
    private RestRequest request;
    private RestResponse response;
    private String dateTime;
    private String customErrorMessage;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public RestRequest getRequest() {
        return request;
    }

    public void setRequest(RestRequest request) {
        this.request = request;
    }

    public RestResponse getResponse() {
        return response;
    }

    public void setResponse(RestResponse response) {
        this.response = response;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCustomErrorMessage() {
        return customErrorMessage;
    }

    public void setCustomErrorMessage(String customErrorMessage) {
        this.customErrorMessage = customErrorMessage;
    }
}
