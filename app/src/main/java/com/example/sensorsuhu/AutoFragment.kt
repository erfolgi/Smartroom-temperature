package com.example.sensorsuhu


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sensorsuhu.api.ApiClient
import com.example.sensorsuhu.api.ApiInterface
import com.example.sensorsuhu.model.SuhuModel
import com.example.sensorsuhu.model.SuhuResponse
import com.example.sensorsuhu.presenter.AutoPresenter
import com.example.sensorsuhu.view.AutoView
import kotlinx.android.synthetic.main.fragment_auto.*
import kotlinx.android.synthetic.main.fragment_manual.*
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*


class AutoFragment : Fragment(), AutoView {

    lateinit var autoPresenter: AutoPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val apiInterface : ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call : Call<SuhuResponse> = apiInterface.getSuhuItem()
        autoPresenter = AutoPresenter(call,this,context!!)
        autoPresenter.getSuhuAutoItem()

        return inflater.inflate(R.layout.fragment_auto, container, false)
    }

    override fun showItemSuhu(listSuhu: ArrayList<SuhuModel>) {
        var lastSuhu = listSuhu.last()
        tv_autosuhu.text = lastSuhu.field_1.toString() + " C"

        var date : String = lastSuhu.date_time!!
        var slicedDate1 = date.replace("T"," ")
        var slicedDate2 = slicedDate1.replace("Z","")

        val dateConvert = gmtFormat(slicedDate2)
        val formatDate = SimpleDateFormat("E, dd-MM-yyyy\nHH:mm:ss", Locale(slicedDate2))
        val formatedDate = formatDate.format(dateConvert)
        tv_autodate.text = formatedDate
    }
    fun gmtFormat(dateP : String?) : Date?{
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale(dateP))
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val dateFormatted = "$dateP"
        return formatter.parse(dateFormatted)
    }

}
