package com.arcthos.smartframework.smartorm.repository;

import com.arcthos.smartframework.smartorm.SmartObject;
import com.google.gson.Gson;
import com.salesforce.androidsdk.smartstore.store.SmartStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vinicius Damiati on 05-Oct-17.
 */

public abstract class Repository<T extends SmartObject> {
    private final SmartStore store;
    private final String soup;
    private final Class<T> typeClass;

    public Repository(final SmartStore store, final String soup, final Class<T> typeClass) {
        this.store = store;
        this.soup = soup;
        this.typeClass = typeClass;
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
}
