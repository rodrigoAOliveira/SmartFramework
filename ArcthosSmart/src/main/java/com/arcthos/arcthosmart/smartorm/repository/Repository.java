package com.arcthos.arcthosmart.smartorm.repository;

import android.util.Log;

import com.arcthos.arcthosmart.annotations.SObject;
import com.arcthos.arcthosmart.smartorm.Condition;
import com.arcthos.arcthosmart.smartorm.SmartObject;
import com.arcthos.arcthosmart.smartorm.SmartObjectConstants;
import com.arcthos.arcthosmart.smartorm.SmartSelect;
import com.google.gson.Gson;
import com.salesforce.androidsdk.smartstore.store.QuerySpec;
import com.salesforce.androidsdk.smartstore.store.SmartStore;
import com.salesforce.androidsdk.smartsync.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicius Damiati on 05-Oct-17.
 */

public abstract class Repository<T extends SmartObject> {
    protected final SmartStore store;
    private String soup;
    private final Class<T> typeClass;

    public Repository(final SmartStore store, final Class<T> typeClass) {
        this.store = store;
        this.typeClass = typeClass;
        getSoup();
    }

    public T create(T model) throws JSONException {
        Gson gson = new Gson();
        String serializedModel = gson.toJson(model);

        JSONObject json = new JSONObject(serializedModel);

        JSONObject response = store.create(soup, json);
        return gson.fromJson(response.toString(), typeClass);

    }

    public List<T> createAll(List<T> models) throws JSONException {
        List<T> responses = new ArrayList<>();

        if (models == null) {
            return responses;
        }

        if(models.isEmpty()) {
            return responses;
        }

        for(T model : models) {
            responses.add(create(model));
        }

        return responses;
    }

    public T update(T model) throws JSONException, RegisterNotCreatedException {
        if(model.getSoupEntryId() == -1) {
            throw new RegisterNotCreatedException("You can't update an register that was not created yet.");
        }

        Gson gson = new Gson();
        String serializedModel = gson.toJson(model);

        JSONObject json = new JSONObject(serializedModel);

        JSONObject response = store.update(soup, json, model.getSoupEntryId());
        return gson.fromJson(response.toString(), typeClass);
    }

    public List<T> updateAll(List<T> models) throws JSONException, RegisterNotCreatedException {
        List<T> responses = new ArrayList<>();

        if (models == null) {
            return responses;
        }

        if(models.isEmpty()) {
            return responses;
        }

        for(T model : models) {
            responses.add(update(model));
        }

        return responses;
    }

    public T upsert(T model) throws JSONException {
        Gson gson = new Gson();
        String serializedModel = gson.toJson(model);

        JSONObject json = new JSONObject(serializedModel);

        JSONObject response = store.upsert(soup, json);
        return gson.fromJson(response.toString(), typeClass);

    }

    public List<T> upsertAll(List<T> models) throws JSONException {
        List<T> responses = new ArrayList<>();

        if (models == null) {
            return responses;
        }

        if(models.isEmpty()) {
            return responses;
        }

        for(T model : models) {
            responses.add(upsert(model));
        }

        return responses;
    }

    public boolean delete(T model) {
        if(model.getSoupEntryId() == -1) {
            return false;
        }

        store.delete(soup, model.getSoupEntryId());
        return true;
    }

    public boolean deleteAll(List<T> models) {
        if (models == null) {
            return false;
        }

        if(models.isEmpty()) {
            return false;
        }

        List<T> validModels = new ArrayList<>();
        for(T model : models) {
            if(model.getSoupEntryId() == -1) {
                continue;
            }
            validModels.add(model);
        }

        Long[] soupEntryIds = new Long[validModels.size()];
        for(int i = 0; i < validModels.size(); i++) {
            soupEntryIds[i] = validModels.get(i).getSoupEntryId();
        }

        store.delete(soup, soupEntryIds);
        return true;
    }

    public T find(String id) {
        if(id == null) {
            return null;
        }

        T model = SmartSelect.from(store, typeClass)
                .where(Condition.prop(Constants.ID).eq(id))
                .first();

        return model;
    }

    public T findByEntryId(long entryId) {
        T model = SmartSelect.from(store, typeClass)
                .where(Condition.prop(SmartObjectConstants.SOUP_ENTRY_ID).eq(entryId))
                .first();

        return model;
    }

    public List<T> findAll() {
        return SmartSelect.from(store, typeClass).list();
    }

    public List<T> findAllWithLimit(int limit) {
        return SmartSelect.from(store, typeClass)
                .limit(String.valueOf(limit))
                .list();
    }

    public List<T> findAllOrderByAsc(String fieldName) {
        return SmartSelect.from(store, typeClass)
                .orderBy(fieldName)
                .list();
    }

    public List<T> findAllOrderByDesc(String fieldName) {
        return SmartSelect.from(store, typeClass)
                .orderByDesc(fieldName)
                .list();
    }

    public int countLocalRegisters() {
        if (!store.hasSoup(soup)) {
            return 0;
        }

        String query = "Select" +
                "               count({" + soup + ":" + SmartObjectConstants.ID + "}) " +
                "       From " +
                "               {" + soup + "} " +
                "       Where " +
                "               {" + soup + ":" + SmartObjectConstants.LOCAL + "} = 'true'";
        QuerySpec querySpec = QuerySpec.buildSmartQuerySpec(query, 1);

        JSONArray results;
        int result = 0;
        try {
            results = store.query(querySpec, 0);
            result = results.getJSONArray(0).getInt(0);
        } catch (Exception e) {
            Log.e(Repository.class.getSimpleName(), e.getMessage(), e);
        }

        return result;
    }

    private void getSoup() {
        if(!typeClass.isAnnotationPresent(SObject.class)) {
            Log.e(SmartObject.class.getSimpleName() + "::GET_SOUP", "SObject annotation missing in model class: " + typeClass.getSimpleName());
            this.soup = "";
            return;
        }

        for(Annotation annotation : typeClass.getAnnotations()) {
            if(annotation instanceof SObject){
                this.soup = ((SObject)annotation).value();
            }
        }
    }
}
