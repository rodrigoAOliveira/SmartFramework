package com.salesforce.androidsdk.smartsync.target;

import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Vinicius Damiati on 18-Apr-18.
 */

public class SyncDownError {
    private String query;
    private RestRequest request;
    private RestResponse response;
    private JSONObject responseJson;
    private JSONArray records;

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

    public JSONObject getResponseJson() {
        return responseJson;
    }

    public void setResponseJson(JSONObject responseJson) {
        this.responseJson = responseJson;
    }

    public JSONArray getRecords() {
        return records;
    }

    public void setRecords(JSONArray records) {
        this.records = records;
    }
}