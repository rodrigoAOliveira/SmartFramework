package com.arcthos.smartframework.smartintegration.helpers;

import android.util.Log;

import com.arcthos.smartframework.annotations.Ignore;
import com.arcthos.smartframework.annotations.SObject;
import com.arcthos.smartframework.annotations.Sync;
import com.arcthos.smartframework.smartorm.SObjectAnnotationNotFoundException;
import com.arcthos.smartframework.smartorm.SmartObject;
import com.google.gson.annotations.SerializedName;
import com.salesforce.androidsdk.smartstore.store.IndexSpec;
import com.salesforce.androidsdk.smartstore.store.SmartStore;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Vinicius Damiati on 14-Oct-17.
 */

public class ModelBuildingHelper<T> {
    private Class<T> modelClass;

    public ModelBuildingHelper(Class<T> modelClass) {
        this.modelClass = modelClass;
    }

    public String getSObjectName() {
        if(!modelClass.isAnnotationPresent(SObject.class)) {
            try {
                throw new SObjectAnnotationNotFoundException("SObject annotation missing in model class: " + modelClass.getSimpleName());
            } catch (SObjectAnnotationNotFoundException e) {
                Log.e(SmartObject.class.getSimpleName(), e.getMessage(), e);
                return null;
            }
        }

        for(Annotation annotation : modelClass.getAnnotations()) {
            if(annotation instanceof SObject){
                return ((SObject)annotation).value();
            }
        }

        return null;
    }

    public IndexSpec[] getIndexSpecs() {
        List<IndexSpec> indexSpecs = new ArrayList<>();
        List<Field> fields = Arrays.asList(modelClass.getDeclaredFields());
        List<Field> superClassFields = Arrays.asList(modelClass.getSuperclass().getDeclaredFields());

        getIndexSpecsByCollection(indexSpecs, superClassFields);
        getIndexSpecsByCollection(indexSpecs, fields);

        IndexSpec[] indexSpecsArray = new IndexSpec[indexSpecs.size()];
        return indexSpecs.toArray(indexSpecsArray);
    }

    private void getIndexSpecsByCollection(List<IndexSpec> indexSpecs, List<Field> fields) {
        for(Field field : fields) {
            if(field.isAnnotationPresent(Ignore.class)) {
                continue;
            }

            String fieldName = "";

            if(field.isAnnotationPresent(SerializedName.class)) {
                for(Annotation annotation : field.getAnnotations()) {
                    if(annotation instanceof SerializedName){
                        fieldName = ((SerializedName)annotation).value();
                        break;
                    }
                }
            } else {
                fieldName = field.getName();
            }

            IndexSpec indexSpec = new IndexSpec(fieldName, SmartStore.Type.string);
            indexSpecs.add(indexSpec);
        }
    }

    public List<String> getFieldsToSyncUp() {
        return getFieldsToSync(false);
    }

    public List<String> getFieldsToSyncDown() {
        return getFieldsToSync(true);
    }

    private List<String> getFieldsToSync(boolean syncDown) {
        List<String> fieldsToSync = new ArrayList<>();

        List<Field> fields = Arrays.asList(modelClass.getDeclaredFields());
        List<Field> superClassFields = Arrays.asList(modelClass.getSuperclass().getDeclaredFields());

        getFieldsToSyncByCollection(fieldsToSync, superClassFields, syncDown);
        getFieldsToSyncByCollection(fieldsToSync, fields, syncDown);

        return fieldsToSync;
    }

    private void getFieldsToSyncByCollection(List<String> fieldsToSync, List<Field> fields, boolean syncDown) {
        for(Field field : fields) {
            if (field.isAnnotationPresent(Ignore.class)) {
                continue;
            }

            String fieldName = "";

            if(field.isAnnotationPresent(SerializedName.class)) {
                for(Annotation annotation : field.getAnnotations()) {
                    if(annotation instanceof SerializedName){
                        fieldName = ((SerializedName)annotation).value();
                        break;
                    }
                }
            } else {
                fieldName = field.getName();
            }

            if(!field.isAnnotationPresent(Sync.class)) {
                fieldsToSync.add(fieldName);
                continue;
            }

            for(Annotation annotation : field.getAnnotations()) {
                if(annotation instanceof Sync){
                    if(syncDown) {
                        if(((Sync)annotation).down()) {
                            fieldsToSync.add(fieldName);
                        }
                    } else {
                        if(((Sync)annotation).up()) {
                            fieldsToSync.add(fieldName);
                        }
                    }
                }
            }
        }
    }
}
