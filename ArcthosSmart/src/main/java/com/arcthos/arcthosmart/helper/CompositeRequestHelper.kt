package com.arcthos.arcthosmart.helper

import com.arcthos.arcthosmart.smartorm.SmartObject
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.JsonObject
import com.arcthos.arcthosmart.annotations.CalculatedField
import com.arcthos.arcthosmart.annotations.IgnoreOnUpdate
import com.arcthos.arcthosmart.annotations.ReferenceField
import com.arcthos.arcthosmart.model.compositeRequest.CompositeRequestConstants
import com.arcthos.arcthosmart.model.graphRequest.GraphRequest
import java.lang.reflect.Field

object CompositeRequestHelper {

    fun getCalculatedFields(type: Class<out SmartObject>): List<String> {
        val fields = type.declaredFields
        val calculatedFields = arrayListOf<String>()

        for (field in fields) {
            if (field.isAnnotationPresent(CalculatedField::class.java)) {
                calculatedFields.add(getJsonPropertyName(field))
            }
        }
        return calculatedFields
    }

    fun getReferenceFields(type: Class<out SmartObject>) : ArrayList<String> {
        val fields = type.declaredFields
        val referencedFields = arrayListOf<String>()

        for (field in fields) {
            for ( i in field.annotations.indices) {
                val annotation = field.annotations[i]
                if (annotation is ReferenceField) {
                    referencedFields.add(getJsonPropertyName(field))
                    break
                }
            }
        }

        return referencedFields
    }

    fun getIgnoreOnUpdateFields(type: Class<out SmartObject>): List<String> {
        val fields = type.declaredFields
        val ignoreOnUpdateFields = arrayListOf<String>()

        for (field in fields) {
            if (field.isAnnotationPresent(IgnoreOnUpdate::class.java)) {
                ignoreOnUpdateFields.add(getJsonPropertyName(field))
            }
        }
        return ignoreOnUpdateFields
    }

    fun getReferencedClass(type: Class<out SmartObject>, referenceField: String) : String{
        val fields = type.declaredFields
        var referencedClass = ""

        for (field in fields) {
            for ( i in field.annotations.indices) {
                if (!field.name.contains(referenceField))
                    continue

                val annotation = field.annotations[i]
                if (annotation is ReferenceField) {
                    referencedClass = annotation.referencedClass
                    break
                }
            }
        }

        return referencedClass
    }

    fun getJsonPropertyName(field : Field) : String{
        var name = ""

        for ( i in field.annotations.indices) {
            val annotation = field.annotations[i]
            if (annotation is JsonProperty) {
                name = annotation.value
                break
            }
        }

        return name
    }

    fun getIdFromJsonObject(jsonObject : JsonObject) : String {
        return if(jsonObject["Id"] != null)
            transformToReferenceId(jsonObject["Id"].asString)
        else
            transformToReferenceId(jsonObject["id"].asString)
    }

    fun transformToReferenceId(id : String) : String{
        return id.replace("-", "")
    }

    fun transformReferenceId(referenceId : String) : String{
        if(referenceId.length<=18)
            return referenceId

        return referenceId.substring(0,8) + "-" + referenceId.substring(8,12) + "-" +
                referenceId.substring(12,16) + "-" + referenceId.substring(16,20) + "-" +
                referenceId.substring(20,32)
    }

    fun getMethodFromJson(jsonObject : JsonObject, id : String): String {
        return when {
            id.length > 18 -> CompositeRequestConstants.METHOD_POST
            getIsDeletedFromJsonObject(jsonObject) -> CompositeRequestConstants.METHOD_DELETE
            getIsUpdatedFromJsonObject(jsonObject) -> CompositeRequestConstants.METHOD_PATCH
            else -> ""
        }
    }

    private fun getIsDeletedFromJsonObject(jsonObject : JsonObject) : Boolean {
        if(jsonObject["__locally_deleted__"] != null)
            return jsonObject["__locally_deleted__"].asBoolean

        return jsonObject["locallyDeleted"].asBoolean
    }

    private fun getIsUpdatedFromJsonObject(jsonObject : JsonObject) : Boolean {
        if(jsonObject["__locally_updated__"] != null)
            return jsonObject["__locally_updated__"].asBoolean

        return jsonObject["locallyUpdated"].asBoolean
    }

    fun prepareGraphs(graphs: List<GraphRequest>){
        graphs.forEach{ graph ->
            graph.compositeRequests.forEach{
                it.body.remove("attributes")
                it.body.remove("Id")
                it.body.remove("__sync_id__")
                it.body.remove("_soupEntryId")
                it.body.remove("LastModifiedDate")
                it.body.remove("IsDeleted")
                it.body.remove("__local__")
                it.body.remove("local")
                it.body.remove("_soupLastModifiedDate")
                it.body.remove("__locally_created__")
                it.body.remove("__locally_updated__")
                it.body.remove("__locally_deleted__")
                it.body.remove("locallyModified")
            }
        }
    }
}