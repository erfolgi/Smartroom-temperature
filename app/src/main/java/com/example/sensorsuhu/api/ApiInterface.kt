package com.example.sensorsuhu.api

import com.example.sensorsuhu.model.SuhuModel
import retrofit2.Call
import retrofit2.http.GET




interface ApiInterface{
    @GET("channels/740883/field/1.json")
    fun getSuhuItem() : Call<SuhuModel>
}