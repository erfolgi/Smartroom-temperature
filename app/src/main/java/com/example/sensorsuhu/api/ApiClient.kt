package com.example.sensorsuhu.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient{
    companion object {
        fun getClient() : Retrofit{
            val baseURL = "https://api.thingspeak.com/"
            return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}