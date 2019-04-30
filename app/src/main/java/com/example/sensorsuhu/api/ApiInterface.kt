package com.example.sensorsuhu.api

import com.example.sensorsuhu.model.SuhuModel
import com.example.sensorsuhu.model.SuhuResponse
import com.example.sensorsuhu.model.WriteResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiInterface{
    @GET("channels/740883/feeds.json?results=5")
    fun getSuhuItem() : Call<SuhuResponse>

    @GET("channels/740883/fields/0.json?results=1")
    fun getLastDate() : Call<SuhuResponse>

    @POST("update.json")
    @FormUrlEncoded
    fun requestWrite(@Field("api_key") key:String?="MEXGF0FJU7KARF27",
                     @Field("field1") f1:String?,
                     @Field("field2") f2:String?,
                     @Field("field3") f3:String?,
                     @Field("field4") f4:String?,
                     @Field("field5") f5:String?): Call<WriteResponse>
}