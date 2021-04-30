package com.arcthos.arcthosmart.helper;

import com.arcthos.arcthosmart.smartorm.SmartObject;

import java.util.List;

public class SmartObjectHelper<T extends SmartObject> {
    public T setSyncVariables(T smartObject) {
        if (smartObject.getId().length() > 18) {
            smartObject.prepareCreate();
        } else {
            smartObject.prepareUpdate();
        }

        return smartObject;
    }

    public List<T> setSyncVariables(List<T> smartObjects) {
        for (T smartObject : smartObjects) {
            smartObject = setSyncVariables(smartObject);
        }

        return smartObjects;
    }
}
