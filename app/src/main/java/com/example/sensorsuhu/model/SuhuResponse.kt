package com.example.sensorsuhu.model

import com.google.gson.annotations.SerializedName







data class SuhuResponse(
    @SerializedName("channel")
    var channel : Channel,
    @SerializedName("feeds")
    var feeds : ArrayList<SuhuModel>
)