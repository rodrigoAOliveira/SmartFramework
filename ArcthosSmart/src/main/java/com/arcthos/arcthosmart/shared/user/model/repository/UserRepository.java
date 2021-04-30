package com.arcthos.arcthosmart.shared.user.model.repository;

import com.arcthos.arcthosmart.smartorm.repository.Repository;
import com.salesforce.androidsdk.smartstore.store.SmartStore;

import java.util.List;

import com.arcthos.arcthosmart.shared.user.model.User;
import com.arcthos.arcthosmart.shared.user.model.constants.UserConstants;

public class UserRepository extends Repository<User> {
    public UserRepository(SmartStore store) {
        super(store, User.class);
    }

    public List<User> findUsers() {
        return getSmartSelect()
                .list();
    }

    public List<User> findByIds(List<String> ids) {
        return getSmartSelect()
                .in(ids, UserConstants.ID)
                .list();
    }
}
