package com.arcthos.smartframework.smartintegration;

import com.salesforce.androidsdk.smartsync.util.SyncState;

import org.json.JSONObject;

/**
 * Created by Vinicius Damiati on 16-Oct-17.
 */

public interface SyncCallback {
    void onUpSuccess(SyncState sync, SyncState.Status status, String sObjectName, JSONObject jsonObject);
    void onUpFailure(SyncState sync);
    void onDownSuccess(SyncState sync, int size, String sObjectName);
    void onDownFailure(SyncState sync);
}
