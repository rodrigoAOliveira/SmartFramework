package com.arcthos.smartframework.smartintegration;

import android.os.AsyncTask;

import com.salesforce.androidsdk.accounts.UserAccount;
import com.salesforce.androidsdk.smartstore.store.IndexSpec;
import com.salesforce.androidsdk.smartstore.store.SmartStore;
import com.salesforce.androidsdk.smartsync.app.SmartSyncSDKManager;

/**
 * Created by Vinicius Damiati on 11-Oct-17.
 */

public class BaseSobjectSyncher<T extends BaseSobject>{

    public static final Integer LIMIT = 50000;

    private final UserAccount currentUser;
    private final SmartStore smartStore;
    private final Class<T> type;

    private String soup;
    private String tag;
    private String where;
    private IndexSpec[] indexSpec;

    public BaseSobjectSyncher(final Class<T> type) {
        this.currentUser = SmartSyncSDKManager.getInstance().getUserAccountManager().getCurrentUser();
        this.smartStore = SmartSyncSDKManager.getInstance().getSmartStore(currentUser);
        this.type = type;

        invokeParameters();
    }

    private String invoke(String field) {
        try {
            return (String) type.getField(field).get(null);
        } catch (Exception e) {
            return "";
        }
    }

    private IndexSpec[] invokeIndex(String field) {
        try {
            return (IndexSpec[]) type.getField(field).get(null);
        } catch (Exception e) {
            return null;
        }
    }

    private String[] invokeArray(String field) {
        try {
            return (String[]) type.getField(field).get(null);
        } catch (Exception e) {
            return null;
        }
    }

    private void invokeParameters() {
        soup = invoke("SOUP");
        tag = invoke("TAG");
        indexSpec = invokeIndex("INDEX_SPEC");
        where = getDefaultWhere() + invoke("WHERE");
    }

    private String getDefaultWhere() {
        return null;
    }

    public synchronized void syncUp(final boolean doSyncdownAfter, final String id) {

    }

    public synchronized void syncDown() {

    }
}
