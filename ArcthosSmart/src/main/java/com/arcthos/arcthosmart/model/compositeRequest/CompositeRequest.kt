package com.arcthos.arcthosmart.model.compositeRequest

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class CompositeRequest(
    @field:SerializedName(CompositeRequestConstants.METHOD)
    val method: String,
    @field:SerializedName(CompositeRequestConstants.REFERENCE_ID)
    val referenceId: String,
    @field:SerializedName(CompositeRequestConstants.BODY)
    val body: JsonObject,
    @field:SerializedName(CompositeRequestConstants.URL)
    val url: String
)