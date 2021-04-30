package com.arcthos.arcthosmart.shared.picklist.repository;

import com.arcthos.arcthosmart.smartorm.Condition;
import com.arcthos.arcthosmart.smartorm.SmartObjectConstants;
import com.arcthos.arcthosmart.smartorm.repository.Repository;
import com.salesforce.androidsdk.smartstore.store.SmartStore;

import java.util.List;

import com.arcthos.arcthosmart.shared.picklist.Picklist;

public class PicklistRepository extends Repository<Picklist> {
    public PicklistRepository(SmartStore store) {
        super(store, Picklist.class);
    }

    public List<Picklist> findPicklistFor(String sobject, String fieldName) {
        return getSmartSelect().where(
                Condition.prop("sobject").eq(sobject),
                Condition.prop("fieldName").eq(fieldName)
        ).list();
    }

    public List<Picklist> findByIds(List<String> ids) {
        return getSmartSelect().in(ids, SmartObjectConstants.ID).list();
    }
}
