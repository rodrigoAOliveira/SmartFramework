import com.arcthos.arcthosmart.helper.CompositeRequestHelper

import com.arcthos.arcthosmart.smartorm.SmartObject
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.arcthos.arcthosmart.model.compositeRequest.CompositeRequest
import com.arcthos.arcthosmart.model.compositeRequest.CompositeRequestConstants
import com.arcthos.arcthosmart.model.graphRequest.GraphRequest
import java.util.*

class CompositeGraphHandler {

    private val gson = Gson()
    private val graphs = ArrayList<GraphRequest>()
    val requestWithReference = ArrayList<JsonObject>()

    fun addGraph(graphId : Int){
        val g = GraphRequest(graphId, arrayListOf())
        graphs.add(g)
    }

    fun jsonGraphs(): List<GraphRequest>{
        CompositeRequestHelper.prepareGraphs(graphs.toList())
        return graphs
    }

    fun addCompositeRequest(
        graphPosition: Int,
        json: String,
        smartObject: Class<out SmartObject>,
        calculatedFields: List<String>) : Boolean{

        val jsonObject = gson.fromJson(json, JsonObject::class.java)
        val id = CompositeRequestHelper.getIdFromJsonObject(jsonObject)
        val className = smartObject.simpleName
        val method = CompositeRequestHelper.getMethodFromJson(jsonObject, id)
        val position = 0

        if(method.isEmpty()){
            return false
        }

        val referenceField = CompositeRequestHelper.getReferenceField(smartObject)

        if (referenceField.isNotEmpty() && jsonObject[referenceField] != null) {
            if (jsonObject[referenceField].toString() == "null"){
                jsonObject.remove(referenceField)
            } else {
                jsonObject.addProperty(
                    referenceField,
                    "@{" + jsonObject[referenceField].toString() + ".id}"
                )
                requestWithReference.add(jsonObject)
                return true
            }
        }

        if (position == 0)
            addGraph(graphPosition + 1)

        addCompositeRequestToGraph(
            smartObject,
            calculatedFields,
            jsonObject,
            graphPosition,
            method,
            id,
            className
        )

        return true
    }

    fun addChildCompositeRequest(
        graphPosition: Int,
        parentId: String,
        json: String,
        smartObject: Class<out SmartObject>,
        referenceField: String,
        calculatedFields: List<String> ){

        val jsonObject = gson.fromJson(json, JsonObject::class.java)
        val id = CompositeRequestHelper.getIdFromJsonObject(jsonObject)
        val className = smartObject.simpleName
        val method = CompositeRequestHelper.getMethodFromJson(jsonObject, id)

        if(method.isEmpty())
            return

        if (jsonObject[referenceField] != null && jsonObject[referenceField].toString() ==
            "\"" + CompositeRequestHelper.transformReferenceId(parentId) + "\"") {
            jsonObject.addProperty(referenceField, "@{$parentId.id}")
        } else {
            addGraph(graphPosition)
        }

        addCompositeRequestToGraph(
            smartObject,
            calculatedFields,
            jsonObject,
            graphPosition,
            method,
            id,
            className
        )

    }

    private fun addCompositeRequestToGraph(
        smartObject: Class<out SmartObject>,
        calculatedFields: List<String>,
        jsonObject: JsonObject,
        graphPosition: Int,
        method: String,
        id: String,
        className: String
    ) {
        for (field in smartObject.declaredFields) {
            val name = CompositeRequestHelper.getJsonPropertyName(field)
            if (calculatedFields.contains(name))
                jsonObject.remove(name)
        }

        graphs[graphPosition].compositeRequests.add(
            CompositeRequest(
                method,
                id,
                jsonObject,
                prepareURL(className, method, id)
            )
        )
    }

    fun addChildCompositeRequestSynchedParent(
        graphPosition: Int,
        parentId: String,
        json: String,
        smartObject: Class<out SmartObject>,
        referenceField: String,
        calculatedFields: List<String> ){

        val jsonObject = gson.fromJson(json, JsonObject::class.java)
        val id = CompositeRequestHelper.getIdFromJsonObject(jsonObject)
        val className = smartObject.simpleName
        val method = CompositeRequestHelper.getMethodFromJson(jsonObject, id)

        if(method.isEmpty()){
            if(graphs[graphPosition].compositeRequests.isEmpty())
                graphs.removeAt(graphPosition)
            return
        }

        if (jsonObject[referenceField] != null && jsonObject[referenceField].toString() != "null") {
            jsonObject.addProperty(referenceField, parentId)
        } else {
            if(graphs[graphPosition].compositeRequests.isEmpty())
                graphs.removeAt(graphPosition)
            return
        }

        for(field in smartObject.declaredFields){
            val name = CompositeRequestHelper.getJsonPropertyName(field)
            if(calculatedFields.contains(name))
                jsonObject.remove(name)
        }

        graphs[graphPosition].compositeRequests.add(
            CompositeRequest(
                method,
                id,
                jsonObject,
                prepareURL(className, method, id)
            )
        )

    }

    private fun prepareURL(className : String, method : String, id : String) : String{
        var url = CompositeRequestConstants.BASE_URL + className
        if(method == CompositeRequestConstants.METHOD_PATCH ||
            method == CompositeRequestConstants.METHOD_DELETE)
            url += "/$id"

        return url
    }

}