package com.arcthos.arcthosmart.smartintegration

import com.arcthos.arcthosmart.annotations.SObject
import com.arcthos.arcthosmart.smartorm.SmartObject
import com.arcthos.arcthosmart.smartorm.SmartSelect
import com.arcthos.arcthosmart.smartorm.repository.Repository
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.salesforce.androidsdk.accounts.UserAccount
import com.salesforce.androidsdk.smartstore.store.SmartStore
import okhttp3.OkHttpClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import com.arcthos.arcthosmart.helper.CompositeGraphHandler
import com.arcthos.arcthosmart.helper.CompositeRequestHelper
import com.arcthos.arcthosmart.model.compositeResponse.CompositeResponse
import com.arcthos.arcthosmart.model.graphRequest.EnvelopeGraphRequest
import com.arcthos.arcthosmart.model.graphRequest.GraphRequest
import com.arcthos.arcthosmart.model.responseGraphs.EnvelopeResponseGraphs
import com.arcthos.arcthosmart.network.RetrofitCompositeService
import com.arcthos.arcthosmart.helper.DateHelper
import com.arcthos.arcthosmart.model.syncerror.SyncUpErrorLog
import com.arcthos.arcthosmart.network.syncError.SyncUpErrorLogRepository
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CompositeSync(
        private val models: List<Class<out SmartObject>>,
        private val userAccount: UserAccount,
        private val smartStore: SmartStore,
        private val syncUpErrorLogRepository: SyncUpErrorLogRepository
) {
    private val retrofit = createRetrofit()
    private val gson = Gson()
    var request = ""
    var response = ""
    var error = ""
    var uploadSuccess = true
    var completed = false
    var lastRequest = false

    fun doCompositeUpload(allModels: List<List<Class<out SmartObject>>>) {

        for (i in allModels.indices){

            val cgh = CompositeGraphHandler()

            val parent = allModels[i][0]
            val parentJsonArray = prepareJsonArray(parent)
            val parentCalculatedFields = CompositeRequestHelper.getCalculatedFields(parent)
            var isDownloaded = false
            var graphPosition = 0

            lastRequest = (i == allModels.indices.last)

            parent.annotations.forEach {
                if(it is SObject && smartStore.hasSoup(it.value))
                    isDownloaded = true
            }

            if(!isDownloaded) {
                completed = true
                break
            }

            if (parentJsonArray.length() == 0){
                if (lastRequest){
                    completed = true
                }
                break
            }

            for (j in 0 until parentJsonArray.length()) {
                val parentJsonObject = parentJsonArray.getJSONArray(j)[0] as JSONObject

                cgh.addGraph(graphPosition + 1)
                if (cgh.addParentCompositeRequest(
                                graphPosition,
                                parentJsonObject.toString(),
                                parent,
                                parentCalculatedFields
                        )){
                    graphPosition++
                }
            }

            if(allModels[i].size > 1) {
                for(j in allModels[i].indices){
                    addChildCompositeRequest(allModels[i][j], cgh)
                }
            }

            jsonCompositeCall(cgh.jsonGraphs())
        }
    }

    private fun addChildCompositeRequest(model: Class<out SmartObject>, cgh: CompositeGraphHandler) {

        val referenceField = CompositeRequestHelper.getReferenceField(model)

        if(referenceField.isEmpty())
            return

        val a = prepareJsonArray(model)
        val calculatedFields = CompositeRequestHelper.getCalculatedFields(model)
        val referencedClassName = CompositeRequestHelper.getReferencedClass(model)

        for(i in 0 until a.length()) {
            val o = a.getJSONArray(i)[0] as JSONObject
            val parentId = o.getString(referenceField)
            var graphPosition = cgh.jsonGraphs().size
            var success = false

            if(cgh.jsonGraphs().isEmpty()){
                cgh.addGraph(1)
                cgh.addChildCompositeRequestSynchedParent(
                        0,
                        parentId,
                        o.toString(),
                        model,
                        referenceField,
                        calculatedFields
                )
                continue
            }

            for (graphId in cgh.jsonGraphs().indices){
                val graph = cgh.jsonGraphs()[graphId]

                if(parentId.length <= 18){
                    for(cr in graph.compositeRequests){
                        if((cr.url.contains(getSoup(model)) && cr.body[referenceField].toString() == "\"" + parentId + "\"") ||
                                (cr.url.contains(referencedClassName) && cr.referenceId == parentId)){
                            graphPosition = graphId
                            success = true
                            break
                        }
                    }
                    continue
                }

                for(cr in graph.compositeRequests){
                    if(cr.url.contains(referencedClassName) &&
                            CompositeRequestHelper.transformReferenceId(cr.referenceId) == parentId){
                        success = true
                        break
                    }
                }
                if(success){
                    cgh.addChildCompositeRequest(
                            graphId,
                            CompositeRequestHelper.transformToReferenceId(parentId),
                            o.toString(),
                            model,
                            referenceField,
                            calculatedFields
                    )
                    break
                }
            }

            if(parentId.length <= 18) {
                if (!success)
                    cgh.addGraph(graphPosition + 1)

                cgh.addChildCompositeRequestSynchedParent(
                        graphPosition,
                        parentId,
                        o.toString(),
                        model,
                        referenceField,
                        calculatedFields
                )
            }
        }
    }

    private fun jsonCompositeCall(graphs: List<GraphRequest>){
        if(graphs.isEmpty() || retrofit == null) {
            if(lastRequest) {
                completed = true
            }
            return
        }

        val envelopeGraphs = EnvelopeGraphRequest()
        envelopeGraphs.graphs = graphs

        request = gson.toJson(envelopeGraphs)
        val jsonObject = gson.fromJson(request, JsonObject::class.java)
        val service = retrofit.create(RetrofitCompositeService::class.java)

        Timber.i("CompositeRequest: $request")

        val call: Call<JsonObject> = service.submit(jsonObject)
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, responseObject: Response<JsonObject>) {
                response = responseObject.body().toString()
                if (responseObject.isSuccessful) {
                    Timber.i("GraphRequest successful. GraphResponse: $response")
                } else {
                    error = "GraphRequest unsuccessful. GraphResponse: $response"
                    syncUpErrorLog("GRAPH_ERROR")
                    uploadSuccess = false
                    return
                }

                val graphResponse = gson.fromJson(response, EnvelopeResponseGraphs::class.java)
                graphResponse.graphs.forEach {
                    it.graphResponses.compositeResponse.forEach { cr ->
                        updateByResponse(cr)
                    }
                }
                if(lastRequest) {
                    completed = true
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                if(lastRequest) {
                    completed = true
                }
                uploadSuccess = false
                Timber.d(call.toString())
            }
        })
    }

    private fun updateByResponse(cr: CompositeResponse) {
        lateinit var body: JsonObject
        val jsonResponse = gson.toJson(cr)

        when {
            cr.body.isJsonObject -> {
                body = cr.body as JsonObject
            }
            cr.body.isJsonArray -> {
                error = "CompositeRequest unsuccessful. CompositeResponse: $jsonResponse"
                syncUpErrorLog("COMPOSITE_REQUEST_ERROR")
                uploadSuccess = false
                return
            }
            else -> {
                return
            }
        }

        val referenceId = CompositeRequestHelper.transformReferenceId(cr.referenceId)
        val success = body["success"]
        val location = cr.httpHeaders["Location"]

        if(location == null || success == null || !success.asBoolean){
            error = "CompositeRequest unsuccessful. CompositeResponse: $jsonResponse"
            syncUpErrorLog("COMPOSITE_REQUEST_ERROR")
            uploadSuccess = false
            return
        } else {
            Timber.i("CompositeRequest unsuccessful. CompositeResponse: %s", jsonResponse)
        }

        models.forEach { model ->
            val name = getSoup(model)
            if(name.isNotEmpty() && location.asString.contains(name))
                updateObject(name, model, referenceId)
        }
    }

    private fun updateObject(
            name: String,
            smartObject: Class<out SmartObject>,
            referenceId: String
    ) {

        val repository = object : Repository<SmartObject>(
                smartStore,
                smartObject as Class<SmartObject>
        ) {}

        val it = repository.find(referenceId)

        smartStore.update(
                name,
                toJSONObject(it),
                it.soupEntryId
        )

        repository.delete(it)
    }

    private fun toJSONObject(smartObject: SmartObject): JSONObject {
        return JSONObject(gson.toJson(smartObject))
    }

    private fun getSoup(model: Class<out SmartObject>): String {
        var name = ""
        model.annotations.forEach {
            if(it is SObject && smartStore.hasSoup(it.value))
                name = it.value
        }
        return name
    }

    private fun createRetrofit() : Retrofit? {

        if(userAccount.instanceServer == null)
            return null

        val okHttpClient: OkHttpClient =
                OkHttpClient().newBuilder().addInterceptor {
                    val originalRequest = it.request()
                    val builder = originalRequest.newBuilder().header(
                            "Authorization",
                            "Bearer " + userAccount.authToken
                    )
                    val newRequest = builder.build()
                    it.proceed(newRequest)
                }.build()

        return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(userAccount.instanceServer)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun syncUpErrorLog(errorType: String) {
        val syncUpErrorLog = SyncUpErrorLog()
        val sdf = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", DateHelper.BRAZILIAN_LOCALE)
        val currentDateandTime = sdf.format(Date())

        Timber.e(error)

        syncUpErrorLog.id = currentDateandTime + "_" + "UPLOAD" + "_" + errorType
        syncUpErrorLog.dateTime = currentDateandTime
        syncUpErrorLog.operation = "UPLOAD"
        syncUpErrorLog.request = request
        syncUpErrorLog.response = response
        syncUpErrorLog.errorType = errorType
        syncUpErrorLog.isDeleted = "false"

        try {
            syncUpErrorLogRepository.upsert(syncUpErrorLog)
        } catch (e: JSONException) {
            Timber.e(e)
        } catch (e: IOException) {
            Timber.e(e)
        }
    }

    private fun prepareJsonArray(model: Class<out SmartObject>): JSONArray {

        val name = getSoup(model)

        if(name.isEmpty())
            return JSONArray()

        if (!model.isAnnotationPresent(SObject::class.java)) {
            Timber.e(
                    SmartObject::class.java.simpleName,
                    "SObject annotation missing in model class: %s",
                    name
            )
            return JSONArray()
        }

        return try {
            SmartSelect.from(smartStore, model).rawList()
        } catch (e: JSONException) {
            Timber.e(e, SmartSelect::class.java.simpleName, e.message)
            JSONArray()
        }
    }

}