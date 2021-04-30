package com.arcthos.arcthosmart.model.responseGraphs

import com.google.gson.annotations.SerializedName
import com.arcthos.arcthosmart.model.graphResponse.GraphResponse

class ResponseGraph(
    @field:SerializedName(ResponseGraphConstants.GRAPH_ID)
    val graphId: Int,
    @field:SerializedName(ResponseGraphConstants.GRAPH_RESPONSE)
    val graphResponses: GraphResponse,
)