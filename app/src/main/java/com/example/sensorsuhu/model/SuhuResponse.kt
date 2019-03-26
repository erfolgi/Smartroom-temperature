package com.example.sensorsuhu.model

import com.google.gson.annotations.SerializedName

data class SuhuResponse(
    @SerializedName("feeds")
    var feeds : ArrayList<SuhuModel>
)