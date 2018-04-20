package com.arcthos.arcthosmart.smartorm;

import android.util.Log;

import com.arcthos.arcthosmart.annotations.SObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salesforce.androidsdk.smartstore.store.QuerySpec;
import com.salesforce.androidsdk.smartstore.store.SmartStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Vinicius Damiati on 03-Oct-17.
 */

public class SmartSelect<T> implements Iterable {

    private static final int LIMIT = 50000;
    private Class<T> record;
    private String soup;
    private String[] arguments;
    private String whereClause = "";
    private String orderBy;
    private String groupBy;
    private String limit;
    private String offset;
    private List<Object> args = new ArrayList<>();
    private SmartStore smartStore;

    public SmartSelect(Class<T> record, SmartStore smartStore) {
        this.record = record;
        this.smartStore = smartStore;
        getSoup();
    }

    public static <T> SmartSelect<T> from(SmartStore smartStore, Class<T> record) {
        return new SmartSelect<T>(record, smartStore);
    }

    public SmartSelect<T> orderBy(String prop) {
        this.orderBy = "{" + soup + ":" + prop + "}";
        return this;
    }

    public SmartSelect<T> orderByDesc(String prop) {
        this.orderBy = "{" + soup + ":" + prop + "}" + " DESC";
        return this;
    }

    public SmartSelect<T> limit(String limit) {
        this.limit = limit;
        return this;
    }

    public SmartSelect<T> where(String whereClause) {
        this.whereClause = whereClause;
        return this;
    }

    public SmartSelect<T> where(Condition... condition) {

        mergeConditions(condition, Condition.Type.AND);

        return this;
    }

    private void mergeConditions(Condition[] conditions, Condition.Type type) {
        StringBuilder toAppend = new StringBuilder("");
        for (Condition condition : conditions) {
            if (toAppend.length() != 0) {
                toAppend.append(" ").append(type.name()).append(" ");
            }

            if (Condition.Check.LIKE.equals(condition.getCheck()) ||
                    Condition.Check.NOT_LIKE.equals(condition.getCheck())) {
                toAppend
                        .append("{")
                        .append(soup)
                        .append(":")
                        .append(condition.getProperty())
                        .append("} ")
                        .append(condition.getCheckSymbol())
                        .append("'")
                        .append(condition.getValue().toString())
                        .append("'");
            } else if (Condition.Check.IS_NULL.equals(condition.getCheck()) ||
                    Condition.Check.IS_NOT_NULL.equals(condition.getCheck())) {
                toAppend
                        .append("{")
                        .append(soup)
                        .append(":")
                        .append(condition.getProperty())
                        .append("} ")
                        .append(condition.getCheckSymbol());
            } else {
                toAppend
                        .append("{")
                        .append(soup)
                        .append(":")
                        .append(condition.getProperty())
                        .append("} ")
                        .append(condition.getCheckSymbol())
                        .append("'")
                        .append(condition.getValue().toString())
                        .append("'");
                args.add(condition.getValue());
            }
        }

        if (!"".equals(whereClause)) {
            whereClause += " " + type.name() + " ";
        }

        whereClause += "(" + toAppend + ")";
    }

    public SmartSelect<T> whereOr(Condition... args) {
        mergeConditions(args, Condition.Type.OR);
        return this;
    }

    public SmartSelect<T> and(Condition... args) {
        mergeConditions(args, Condition.Type.AND);
        return this;
    }

    public SmartSelect<T> or(Condition... args) {
        mergeConditions(args, Condition.Type.OR);
        return this;
    }

    public SmartSelect<T> in(List<String> args, String field) {
        if(args == null) {
            return this;
        }

        if(args.isEmpty()) {
            return this;
        }

        Condition[] conditions = new Condition[args.size()];

        for(int i = 0; i < args.size(); i++) {
            Condition condition = Condition.prop(field).eq(args.get(i));
            conditions[i] = condition;
        }

        return or(conditions);
    }

    public SmartSelect<T> where(String whereClause, String[] args) {
        this.whereClause = whereClause;
        this.arguments = args;
        return this;
    }

    public List<T> list() {
        if(record == null) {
            Log.e(SmartSelect.class.getSimpleName() + "::LIST", "You can only do a RAW call when you are not using a model.");
            return null;
        }

        if(!record.isAnnotationPresent(SObject.class)) {
            Log.e(SmartObject.class.getSimpleName(), "SObject annotation missing in model class: " + record.getSimpleName());
            return null;
        }

        if (arguments == null) {
            arguments = convertArgs(args);
        }

        String sql = toSql();
        QuerySpec querySpec = QuerySpec.buildSmartQuerySpec(sql, LIMIT);

        try {
            JSONArray results = smartStore.query(querySpec, 0);

            ObjectMapper objectMapper = new ObjectMapper();
            List<T> models = new ArrayList<>();

            for (int i = 0; i < results.length(); i++) {
                T model = objectMapper.readValue(results.getJSONArray(i).getJSONObject(0).toString(), record);
                models.add(model);
            }

            return models;

        } catch (JSONException e) {
            Log.e(SmartSelect.class.getSimpleName(), e.getMessage(), e);
            return new ArrayList<>();
        } catch (JsonParseException e) {
            Log.e(SmartSelect.class.getSimpleName(), e.getMessage(), e);
            return new ArrayList<>();
        } catch (JsonMappingException e) {
            Log.e(SmartSelect.class.getSimpleName(), e.getMessage(), e);
            return new ArrayList<>();
        } catch (IOException e) {
            Log.e(SmartSelect.class.getSimpleName(), e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public JSONArray rawList() {
        if (arguments == null) {
            arguments = convertArgs(args);
        }

        if(!record.isAnnotationPresent(SObject.class)) {
            Log.e(SmartObject.class.getSimpleName(), "SObject annotation missing in model class: " + record.getSimpleName());
            return null;
        }

        String sql = toSql();
        QuerySpec querySpec = QuerySpec.buildSmartQuerySpec(sql, LIMIT);

        try {
            return smartStore.query(querySpec, 0);
        } catch (JSONException e) {
            Log.e(SmartSelect.class.getSimpleName(), e.getMessage(), e);
            return new JSONArray();
        }
    }

    //public long count() {
    //    if (arguments == null) {
    //        arguments = convertArgs(args);
    //    }

    //    return SugarRecord.count(record, whereClause, arguments, groupBy, orderBy, limit);
    //}

    public T first() {
        if(record == null) {
            Log.e(SmartSelect.class.getSimpleName() + "::FIRST", "You can only do a RAW call when you are not using a model.");
            return null;
        }

        if(!record.isAnnotationPresent(SObject.class)) {
            Log.e(SmartObject.class.getSimpleName(), "SObject annotation missing in model class: " + record.getSimpleName());
            return null;
        }

        if (arguments == null) {
            arguments = convertArgs(args);
        }

        String sql = toSql();
        QuerySpec querySpec = QuerySpec.buildSmartQuerySpec(sql, 1);

        try {
            JSONArray results = smartStore.query(querySpec, 0);

            ObjectMapper objectMapper = new ObjectMapper();
            List<T> models = new ArrayList<>();

            for (int i = 0; i < results.length(); i++) {
                T model = objectMapper.readValue(results.getJSONArray(i).getJSONObject(0).toString(), record);
                models.add(model);
            }

            return models.size() > 0 ? models.get(0) : null;

        } catch (JSONException e) {
            Log.e(SmartSelect.class.getSimpleName(), e.getMessage(), e);
            return null;
        } catch (NullPointerException e) {
            Log.e(SmartSelect.class.getSimpleName(), e.getMessage(), e);
            return null;
        } catch (JsonParseException e) {
            Log.e(SmartSelect.class.getSimpleName(), e.getMessage(), e);
            return null;
        } catch (JsonMappingException e) {
            Log.e(SmartSelect.class.getSimpleName(), e.getMessage(), e);
            return null;
        } catch (IOException e) {
            Log.e(SmartSelect.class.getSimpleName(), e.getMessage(), e);
            return null;
        }
    }

    public JSONObject rawFirst() {
        if (arguments == null) {
            arguments = convertArgs(args);
        }

        if(!record.isAnnotationPresent(SObject.class)) {
            Log.e(SmartObject.class.getSimpleName(), "SObject annotation missing in model class: " + record.getSimpleName());
            return null;
        }

        String sql = toSql();
        QuerySpec querySpec = QuerySpec.buildSmartQuerySpec(sql, 1);

        try {
            JSONArray results = smartStore.query(querySpec, 0);

            if(results == null) {
                return null;
            }

            if(results.length() > 0) {
                return results.getJSONArray(0).getJSONObject(0);
            }

            return null;

        } catch (JSONException e) {
            Log.e(SmartSelect.class.getSimpleName(), e.getMessage(), e);
            return null;
        } catch (NullPointerException e) {
            Log.e(SmartSelect.class.getSimpleName(), e.getMessage(), e);
            return null;
        }
    }

    private String toSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT {" + soup + ":_soup} FROM {" + soup + "}").append(" ");

        if (whereClause != null && !whereClause.equals("")) {
            sql.append("WHERE ").append(whereClause).append(" ");
        }

        if (orderBy != null) {
            sql.append("ORDER BY ").append(orderBy).append(" ");
        }

        if (limit != null) {
            sql.append("LIMIT ").append(limit).append(" ");
        }

        if (offset != null) {
            sql.append("OFFSET ").append(offset).append(" ");
        }

        return sql.toString();
    }

    private String[] convertArgs(List<Object> argsList) {
        String[] argsArray = new String[argsList.size()];

        for (int i = 0; i < argsList.size(); i++) {
            argsArray[i] = argsList.get(i).toString();
        }

        return argsArray;
    }

    private void getSoup() {
        if(!record.isAnnotationPresent(SObject.class)) {
            Log.e(SmartObject.class.getSimpleName() + "::GET_SOUP", "SObject annotation missing in model class: " + record.getSimpleName());
            this.soup = "";
            return;
        }

        for(Annotation annotation : record.getAnnotations()) {
            if(annotation instanceof SObject){
                this.soup = ((SObject)annotation).value();
            }
        }
    }

    @Override
    public Iterator iterator() {
        return null;
    }
}
