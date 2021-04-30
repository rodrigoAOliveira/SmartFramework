package com.arcthos.arcthosmart.model.responseGraphs

import com.google.gson.annotations.SerializedName

class EnvelopeResponseGraphs {
    @SerializedName("graphs")
    lateinit var graphs: List<ResponseGraph>
}