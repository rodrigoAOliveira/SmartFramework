package com.arcthos.arcthosmart.model.compositeResponse

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class CompositeResponse(
    @field:SerializedName(CompositeResponseConstants.REFERENCE_ID)
    val referenceId: String,
    @field:SerializedName(CompositeResponseConstants.BODY)
    val body: JsonElement,
    @field:SerializedName(CompositeResponseConstants.HTTP_STATUS_CODE)
    val httpStatusCode: String,
    @field:SerializedName(CompositeResponseConstants.HTTP_HEADERS)
    val httpHeaders: JsonObject
)