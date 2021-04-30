package com.arcthos.arcthosmart.model.graphResponse

import com.google.gson.annotations.SerializedName
import com.arcthos.arcthosmart.model.compositeResponse.CompositeResponse

data class GraphResponse(
    @field:SerializedName(GraphResponseConstants.IS_SUCCESSFUL)
    val isSuccessful: Boolean,
    @field:SerializedName(GraphResponseConstants.COMPOSITE_RESPONSE)
    val compositeResponse: ArrayList<CompositeResponse>
)