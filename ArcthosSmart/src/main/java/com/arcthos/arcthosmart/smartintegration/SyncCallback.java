package com.arcthos.arcthosmart.smartintegration;

import com.salesforce.androidsdk.mobilesync.util.SyncState;

import org.json.JSONObject;

/**
 * Created by Vinicius Damiati on 16-Oct-17.
 */

public interface SyncCallback {
    void onUpSuccess(SyncState sync, SyncState.Status status, String sObjectName);
    void onUpFailure(SyncState sync, JSONObject jsonObject);
    void onDownSuccess(SyncState sync, int size, String sObjectName);
    void onDownFailure(SyncState sync);
}
