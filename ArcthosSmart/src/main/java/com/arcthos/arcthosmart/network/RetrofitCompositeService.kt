package com.arcthos.arcthosmart.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface RetrofitCompositeService {
    @Headers("Content-Type: application/json")
    @POST("/services/data/v51.0/composite/graph")
    fun submit(@Body body: JsonObject): Call<JsonObject>
}