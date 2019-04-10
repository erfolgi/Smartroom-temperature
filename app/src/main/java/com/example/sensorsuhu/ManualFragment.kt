package com.example.sensorsuhu


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.sensorsuhu.R
import com.example.sensorsuhu.api.ApiClient
import com.example.sensorsuhu.api.ApiInterface
import com.example.sensorsuhu.model.SuhuModel
import com.example.sensorsuhu.model.SuhuResponse
import com.example.sensorsuhu.presenter.ManualPresenter
import com.example.sensorsuhu.view.ManualView
import kotlinx.android.synthetic.main.fragment_manual.*
import kotlinx.android.synthetic.main.fragment_manual.view.*
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*


class ManualFragment : Fragment(), ManualView {

    lateinit var manualPresenter: ManualPresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_manual, container, false)
        view.toggle_fan.setOnClickListener {
            if(toggle_fan.isChecked){
                Glide.with(this@ManualFragment).load(R.drawable.coldfanon).into(stat_fan)
            }else{
                Glide.with(this@ManualFragment).load(R.drawable.coldfanoff).into(stat_fan)
            }
        }
        view.toggle_lamp.setOnClickListener {
            if(toggle_lamp.isChecked){
//                lampToggle=false
                Glide.with(this@ManualFragment).load(R.drawable.coldlampon).into(stat_lamp)

            }else{
                //lampToggle=true
                Glide.with(this@ManualFragment).load(R.drawable.coldlampoff).into(stat_lamp)
            }
        }

        val apiInterface : ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call : Call<SuhuResponse> = apiInterface.getSuhuItem()
        manualPresenter = ManualPresenter(call, this, context!!)
        manualPresenter.getManualSuhuItem()

        return view
    }

    override fun showItemSuhu(listSuhu: ArrayList<SuhuModel>) {
        val lastSuhu = listSuhu.last()
        tv_manualsuhu.text = lastSuhu.field_1.toString() + " C"

        var date : String = lastSuhu.date_time!!
        var slicedDate1 : String = date.replace("T"," ")
        var slicedDate2 : String = slicedDate1.replace("Z","")
        val dateConvert = gmtFormat(slicedDate2)
        val formatDate = SimpleDateFormat("E, dd-MM-yyyy\nHH:mm:ss", Locale(slicedDate2))
        val formattedDate = formatDate.format(dateConvert)
        tv_manualdate.text = formattedDate
    }

    fun gmtFormat(dateP : String?) : Date?{
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale(dateP))
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val dateFormatted = "$dateP"
        return formatter.parse(dateFormatted)
    }

}
