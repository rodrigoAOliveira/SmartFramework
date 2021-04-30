package com.arcthos.arcthosmart.network.syncError;

import com.arcthos.arcthosmart.model.syncerror.SyncDownErrorLog;
import com.arcthos.arcthosmart.smartintegration.helpers.ModelBuildingHelper;
import com.arcthos.arcthosmart.smartorm.SmartObject;
import com.arcthos.arcthosmart.smartorm.repository.Repository;
import com.salesforce.androidsdk.smartstore.store.IndexSpec;
import com.salesforce.androidsdk.smartstore.store.SmartStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;


import static com.arcthos.arcthosmart.model.syncerror.SyncDownErrorLogConstants.SYNC_DOWN_ERROR_LOG;

public class SyncDownErrorLogRepository extends Repository<SyncDownErrorLog> {
    @Inject
    public SyncDownErrorLogRepository(SmartStore store) {
        super(store, SyncDownErrorLog.class);
        if (!store.hasSoup(SYNC_DOWN_ERROR_LOG)) {
            List<IndexSpec> indexSpecs = new ArrayList<>();
            indexSpecs.addAll(Arrays.asList(new ModelBuildingHelper<>(SyncDownErrorLog.class).getIndexSpecs()));
            indexSpecs.addAll(Arrays.asList(new ModelBuildingHelper<>(SmartObject.class).getIndexSpecs()));
            IndexSpec[] indexSpecsArray = new IndexSpec[indexSpecs.size()];
            indexSpecsArray = indexSpecs.toArray(indexSpecsArray);
            store.registerSoup(SYNC_DOWN_ERROR_LOG, indexSpecsArray);
        }
    }
}
