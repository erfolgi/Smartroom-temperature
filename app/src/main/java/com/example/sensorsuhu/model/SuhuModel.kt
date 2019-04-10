package com.example.sensorsuhu.model

import com.google.gson.annotations.SerializedName

data class SuhuModel(
    @SerializedName("field1")
    var field_1 : String?,
    @SerializedName("field2")
    var field_2 : String?,
    @SerializedName("field3")
    var field_3 : String?,
    @SerializedName("field4")
    var field_4 : String?,
    @SerializedName("field5")
    var field_5 : String?,
    @SerializedName("created_at")
    var date_time : String?
)