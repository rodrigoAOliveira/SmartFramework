package com.arcthos.arcthosmart.model.graphRequest

import com.google.gson.annotations.SerializedName
import com.arcthos.arcthosmart.model.compositeRequest.CompositeRequest

data class GraphRequest(
    @field:SerializedName(GraphRequestConstants.GRAPH_ID)
    val graphId: Int,
    @field:SerializedName(GraphRequestConstants.COMPOSITE_REQUEST)
    val compositeRequests: ArrayList<CompositeRequest>
)
