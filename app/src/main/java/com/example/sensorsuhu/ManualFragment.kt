package com.example.sensorsuhu


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
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
    lateinit var f1: String
    lateinit var f2: String
    lateinit var f3: String
    lateinit var f4: String
    lateinit var f5: String
    lateinit var viewed : View
    lateinit var mHandler: Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewed = inflater.inflate(R.layout.fragment_manual, container, false)
        val apiInterface : ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call : Call<SuhuResponse> = apiInterface.getSuhuItem()
        manualPresenter = ManualPresenter(call, this, context!!)
        this.mHandler = Handler()
        this.mHandler.post(refresher)
        viewed.toggle_lamp.setOnClickListener {
            mHandler.removeCallbacks(refresher)
            mHandler.removeCallbacksAndMessages(null)
            if(toggle_lamp.isChecked){
                manualPresenter.PostData(f1,f2,f3,f4,"1.00")
            }else{
                manualPresenter.PostData(f1,f2,f3,f4,"0.00")
            }
        }
        viewed.toggle_fan.setOnClickListener {
            mHandler.removeCallbacks(refresher)
            mHandler.removeCallbacksAndMessages(null)
            if(toggle_fan.isChecked){
                manualPresenter.PostData(f1,f2,f3,"1.00",f5)
            }else{
                manualPresenter.PostData(f1,f2,f3,"0.00",f5)
            }
        }



        return viewed
    }

    @SuppressLint("SetTextI18n")
    override fun showItemSuhu(listSuhu: ArrayList<SuhuModel>) {
        val lastSuhu = listSuhu.last()
        viewed.tv_manualsuhu.text = lastSuhu.field_1.toString() + "\u00B0C"
        f1=lastSuhu.field_1.toString()
        f2=lastSuhu.field_2.toString()
        f3=lastSuhu.field_3.toString()
        f4=lastSuhu.field_4.toString()
        f5=lastSuhu.field_5.toString()

        var date : String = lastSuhu.date_time!!
        var slicedDate1 : String = date.replace("T"," ")
        var slicedDate2 : String = slicedDate1.replace("Z","")
        val dateConvert = gmtFormat(slicedDate2)
        val formatDate = SimpleDateFormat("E, dd-MM-yyyy\nHH:mm:ss", Locale(slicedDate2))
        val formattedDate = formatDate.format(dateConvert)
        tv_manualdate.text = formattedDate
        Log.e("f2f", "= "+lastSuhu.field_2.toString())
        if (f2=="0"){
            Log.e("f2", "= 00")
            Glide.with(this@ManualFragment).load(R.drawable.coldfanoff).into(viewed.stat_fan)
        }else if (f2=="1"){
            Log.e("f2", "= 01")
            Glide.with(this@ManualFragment).load(R.drawable.coldfanon).into(viewed.stat_fan)
        }

        if (f3=="0"){
            Log.e("f3", "= 00")
            Glide.with(this@ManualFragment).load(R.drawable.coldlampoff).into(viewed.stat_lamp)
        }else if (f3=="1"){
            Log.e("f3", "= 01")
            Glide.with(this@ManualFragment).load(R.drawable.coldlampon).into(viewed.stat_lamp)
        }

        if (f4=="0"){
            viewed.toggle_fan.isChecked=false
        }else if (f4=="1"){
            viewed.toggle_fan.isChecked=true
        }
        if (f5=="0"){
            viewed.toggle_lamp.isChecked=false
        }else if (f5=="1"){
            viewed.toggle_lamp.isChecked=true
        }
         Toast.makeText(context,"refreshed", LENGTH_SHORT).show()
        mHandler.postDelayed(refresher, 10000)

    }

    fun gmtFormat(dateP : String?) : Date?{
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale(dateP))
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val dateFormatted = "$dateP"
        return formatter.parse(dateFormatted)
    }
    private val refresher = Runnable {
        manualPresenter.getManualSuhuItem()
        // Toast.makeText(this@MainActivity,"Delay", LENGTH_SHORT).show()
    }//runnable
}
