package com.example.sensorsuhu


import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.fragment_auto.view.*
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*


class AutoFragment : Fragment(){

    lateinit var myactivity: DrawerActivity
    lateinit var f1: String
    lateinit var f2: String
    lateinit var f3: String
    lateinit var f4: String
    lateinit var f5: String
    lateinit var viewed : View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewed = inflater.inflate(R.layout.fragment_auto, container, false)
        //
        showSuhu()
        viewed.manual_button.setOnClickListener {
            myactivity = activity as DrawerActivity
            myactivity.changeFragment("manual")
        }
        return viewed
    }

     @SuppressLint("SetTextI18n")
     fun showSuhu() {
         myactivity = activity as DrawerActivity
         val lastSuhu = myactivity.getFromActivity()
         f1=lastSuhu.field_1.toString()
         f2=lastSuhu.field_2.toString()
         f3=lastSuhu.field_3.toString()
         f4=lastSuhu.field_4.toString()
         f5=lastSuhu.field_5.toString()
        viewed.tv_autosuhu.text = lastSuhu.field_1.toString() + "°C"

        var date : String = lastSuhu.date_time!!
        var slicedDate1 = date.replace("T"," ")
        var slicedDate2 = slicedDate1.replace("Z","")

        val timeConvert = gmtFormat(slicedDate2)
        val formatDate = SimpleDateFormat("E, dd-MM-yyyy\nHH:mm:ss", Locale(slicedDate2))
        val dateParsed = formatDate.format(timeConvert)
        viewed.tv_autodate.text = dateParsed

         /////////////////////////////
         val f1f=f1.toFloat()
         when {
             f1f in 25.0..30.0 -> {
                 viewed.autolayout.background=(resources.getDrawable(R.drawable.gradasi))
             }
             f1f>30 -> {
                 viewed.autolayout.background=(resources.getDrawable(R.drawable.gradasihot))
             }
             f1f<25 -> {
                 viewed.autolayout.background=(resources.getDrawable(R.drawable.gradasicold))
             }
         }
    }

    fun gmtFormat(dateP : String?) : Date?{
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale(dateP))
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val dateTime = "$dateP"
        return formatter.parse(dateTime)
    }
}
