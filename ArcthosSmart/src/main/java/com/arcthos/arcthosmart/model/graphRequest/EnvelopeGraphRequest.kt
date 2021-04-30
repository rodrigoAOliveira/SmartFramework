package com.arcthos.arcthosmart.model.graphRequest

import com.google.gson.annotations.SerializedName

class EnvelopeGraphRequest {
    @SerializedName("graphs")
    lateinit var graphs: List<GraphRequest>
}