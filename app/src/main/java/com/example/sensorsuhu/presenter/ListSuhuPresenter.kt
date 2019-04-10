package com.example.sensorsuhu.presenter

import android.content.Context
import android.widget.Toast
import com.example.sensorsuhu.model.SuhuModel
import com.example.sensorsuhu.model.SuhuResponse
import com.example.sensorsuhu.view.AutoView
import com.example.sensorsuhu.view.ManualView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListSuhuPresenter (private val call : Call<SuhuResponse>,
                         private val manualView : ManualView,
                         private val context: Context){
    fun getListSuhuItem(){
        var listSuhu : ArrayList<SuhuModel>
        call.enqueue(object : Callback<SuhuResponse>{
            override fun onFailure(call: Call<SuhuResponse>, t: Throwable) {
                Toast.makeText(context, "Gagal ambil item", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<SuhuResponse>, response: Response<SuhuResponse>) {
                listSuhu = response.body()!!.feeds

                try {
                    manualView.showItemSuhu(listSuhu)
                }catch (e: Exception){

                }
            }

        })
    }
}