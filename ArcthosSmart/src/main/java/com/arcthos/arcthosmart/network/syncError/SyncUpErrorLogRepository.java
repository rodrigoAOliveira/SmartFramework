package com.arcthos.arcthosmart.network.syncError;

import com.arcthos.arcthosmart.smartintegration.helpers.ModelBuildingHelper;
import com.arcthos.arcthosmart.smartorm.Condition;
import com.arcthos.arcthosmart.smartorm.SmartObject;
import com.arcthos.arcthosmart.smartorm.SmartObjectConstants;
import com.arcthos.arcthosmart.smartorm.repository.Repository;
import com.salesforce.androidsdk.smartstore.store.IndexSpec;
import com.salesforce.androidsdk.smartstore.store.SmartStore;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import topi.androidsalesforceseedkt.model.syncerror.SyncUpErrorLog;
import topi.androidsalesforceseedkt.model.syncerror.SyncUpErrorLogConstants;


public class SyncUpErrorLogRepository extends Repository<SyncUpErrorLog> {

    @Inject
    public SyncUpErrorLogRepository(SmartStore store) {
        super(store, SyncUpErrorLog.class);
        if (!store.hasSoup(SyncUpErrorLogConstants.SYNC_UP_ERROR_LOG)) {
            List<IndexSpec> indexSpecs = new ArrayList<>();
            indexSpecs.addAll(Arrays.asList(new ModelBuildingHelper<>(SyncUpErrorLog.class).getIndexSpecs()));
            indexSpecs.addAll(Arrays.asList(new ModelBuildingHelper<>(SmartObject.class).getIndexSpecs()));
            IndexSpec[] indexSpecsArray = new IndexSpec[indexSpecs.size()];
            indexSpecsArray = indexSpecs.toArray(indexSpecsArray);
            store.registerSoup(SyncUpErrorLogConstants.SYNC_UP_ERROR_LOG, indexSpecsArray);
        }
    }

    @Override
    public List<SyncUpErrorLog> upsertAll(List<SyncUpErrorLog> models) throws JSONException, IOException {
        List<SyncUpErrorLog> insertedSyncs = findSyncUpErrors(getIds(models));
        List<SyncUpErrorLog> newSyncs = new ArrayList<>(models);
        if (insertedSyncs == null || insertedSyncs.isEmpty()) {
            return super.upsertAll(models);
        }
        for (SyncUpErrorLog insertedSync : insertedSyncs) {
            removeFromList(insertedSync, newSyncs);
        }
        return super.upsertAll(newSyncs);
    }

    private void removeFromList(SyncUpErrorLog insertedSync, List<SyncUpErrorLog> newSyncs) {
        for (int i = 0; i < newSyncs.size(); i++) {
            if (newSyncs.get(i).getId().equals(insertedSync.getId())) {
                newSyncs.remove(i);
                return;
            }
        }
    }

    private List<String> getIds(List<SyncUpErrorLog> models) {
        List<String> ids = new ArrayList<>();
        for (SyncUpErrorLog model : models) {
            ids.add(model.getId());
        }
        return ids;
    }

    public List<SyncUpErrorLog> findSyncUpErrors(List<String> ids) {
        return getSmartSelect().in(ids, SmartObjectConstants.ID).list();
    }

    public List<SyncUpErrorLog> findByObjectAndMessage(String object, String message) {
        return getSmartSelect()
                .where(Condition.prop("sObject").eq(object),
                        Condition.prop("response").likeAll(message))
                .list();
    }
}
