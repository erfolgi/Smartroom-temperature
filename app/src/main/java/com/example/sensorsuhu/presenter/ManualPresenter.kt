package com.example.sensorsuhu.presenter

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.sensorsuhu.api.ApiClient
import com.example.sensorsuhu.api.ApiInterface
import com.example.sensorsuhu.model.SuhuModel
import com.example.sensorsuhu.model.SuhuResponse
import com.example.sensorsuhu.model.WriteResponse
import com.example.sensorsuhu.view.ManualView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ManualPresenter(private val call : Call<SuhuResponse>,
                      private val manualView: ManualView,
                      private val context: Context){
    fun getManualSuhuItem(){
        var listSuhu : ArrayList<SuhuModel>
        //val apiInterface : ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        //val call : Call<SuhuResponse> = apiInterface.getSuhuItem()
        call.cancel()

        call.clone().enqueue(object : Callback<SuhuResponse>{
            override fun onFailure(call: Call<SuhuResponse>, t: Throwable) {
                Toast.makeText(context, "Gagal ambil item", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<SuhuResponse>, response: Response<SuhuResponse>) {
                listSuhu = response.body()!!.feeds
                try {
                    manualView.showItemSuhu(listSuhu)
                }catch (e : Exception){}
            }

        })
    }
    fun cancelCall(){
        call.cancel()
    }

    fun PostData(f1: String, f2: String, f3: String, f4: String, f5: String){
        call.cancel()
        val apiInterface : ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val calls : Call<WriteResponse> = apiInterface.requestWrite("MEXGF0FJU7KARF27",f1,f2,f3,f4,f5)

        calls.enqueue(object : Callback<WriteResponse>{
            override fun onResponse(call: Call<WriteResponse>, response: Response<WriteResponse>) {
                try {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    getManualSuhuItem()
                }catch (e : Exception){}
            }

            override fun onFailure(call: Call<WriteResponse>, t: Throwable) {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }

        })

    }
}