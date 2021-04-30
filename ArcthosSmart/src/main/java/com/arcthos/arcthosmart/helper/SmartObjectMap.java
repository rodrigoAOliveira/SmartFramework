package com.arcthos.arcthosmart.helper;


import com.arcthos.arcthosmart.smartorm.SmartObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Vinicius Damiati on 17-Jan-18.
 */

public class SmartObjectMap<E extends SmartObject> extends HashMap<String, E> {
    public SmartObjectMap() {

    }

    public SmartObjectMap(List<E> smartObjects) {
        for(E smartObject : smartObjects) {
            put(smartObject.getId(), smartObject);
        }
    }
}
