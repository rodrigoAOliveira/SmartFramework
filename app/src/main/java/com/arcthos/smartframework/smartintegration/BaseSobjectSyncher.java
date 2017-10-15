package com.arcthos.smartframework.smartintegration;

import android.util.Log;

import com.arcthos.smartframework.smartorm.Condition;
import com.arcthos.smartframework.smartorm.SmartObject;
import com.arcthos.smartframework.smartorm.SmartSelect;
import com.salesforce.androidsdk.accounts.UserAccount;
import com.salesforce.androidsdk.smartstore.store.IndexSpec;
import com.salesforce.androidsdk.smartstore.store.SmartStore;
import com.salesforce.androidsdk.smartsync.app.SmartSyncSDKManager;
import com.salesforce.androidsdk.smartsync.manager.SyncManager;
import com.salesforce.androidsdk.smartsync.target.SyncUpTarget;
import com.salesforce.androidsdk.smartsync.util.Constants;
import com.salesforce.androidsdk.smartsync.util.SyncOptions;
import com.salesforce.androidsdk.smartsync.util.SyncState;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Vinicius Damiati on 11-Oct-17.
 */

public class BaseSobjectSyncher<T extends SmartObject>{

    public static final Integer LIMIT = 50000;

    private final UserAccount currentUser;
    private final SmartStore smartStore;
    private final SyncManager syncMgr;
    private final Class<T> type;
    private final ModelBuildingHelper modelBuildingHelper;
    private String where;

    public BaseSobjectSyncher(final Class<T> type) {
        this.currentUser = SmartSyncSDKManager.getInstance().getUserAccountManager().getCurrentUser();
        this.smartStore = SmartSyncSDKManager.getInstance().getSmartStore(currentUser);
        this.syncMgr = SyncManager.getInstance(currentUser);
        this.type = type;
        this.modelBuildingHelper = new ModelBuildingHelper(type);
        this.where = getDefaultWhere();
    }

    private String getDefaultWhere() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", new Locale("PT", "BR"));
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        String now = sdf.format(new Date());

        String where = Constants.LAST_MODIFIED_DATE + ">" + now + " ";

        return where;
    }

    public synchronized void syncUp(final boolean doSyncdownAfter) {
        syncUp(doSyncdownAfter, null);
    }

    public synchronized void syncUp(final boolean doSyncdownAfter, final String id) {
        List<String> fieldsSyncUp = modelBuildingHelper.getFieldsToSyncUp();

        SyncUpTarget target;
        if (id == null) {
            target = new SyncUpTarget();
        } else {
            JSONObject object = SmartSelect.from(smartStore, type)
                    .where(Condition.prop(Constants.ID).eq(id))
                    .rawFirst();
            try {
                target = new SyncUpTarget(object);
            } catch (JSONException e) {
                target = new SyncUpTarget();
            }
        }

        final SyncOptions options = SyncOptions.optionsForSyncUp(fieldsSyncUp, SyncState.MergeMode.OVERWRITE);

        try {
            syncMgr.syncUp(target, options, modelBuildingHelper.getSObjectName(), new SyncManager.SyncUpdateCallback() {
                @Override
                public void onUpdate(SyncState sync) {
                    if (SyncState.Status.DONE.equals(sync.getStatus()) || SyncState.Status.FAILED.equals(sync.getStatus())) {
                        //fireSyncUpCompleteIntent(sync.getStatus());

                        try {
                            if (SyncState.Status.DONE.equals(sync.getStatus()) && doSyncdownAfter) {
                                if (id != null)
                                    where = Constants.ID + "='" + id + "'";

                                syncDown();
                            } else if (SyncState.Status.FAILED.equals(sync.getStatus())) {
                                System.out.println(sync.getSoupName());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } catch (JSONException e) {
            Log.e(type.getSimpleName(), "JSONException occurred while parsing", e);
        } catch (SyncManager.SmartSyncException e) {
            Log.e(type.getSimpleName(), "SmartSyncException occurred while attempting to sync up", e);
        } catch (Exception e) {
            Log.e(type.getSimpleName(), "Exception occurred while attempting to sync up", e);
        }
    }

    public synchronized void syncDown() {

    }
}
