package com.example.sensorsuhu


import android.annotation.SuppressLint
import android.os.Build
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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_manual.*
import kotlinx.android.synthetic.main.fragment_manual.view.*
import java.text.SimpleDateFormat
import java.util.*


class ManualFragment : Fragment() {

    //lateinit var manualPresenter: ManualPresenter
    lateinit var myactivity: DrawerActivity
    lateinit var f1: String
    lateinit var f2: String
    lateinit var f3: String
    lateinit var f4: String
    lateinit var f5: String
    lateinit var viewed : View
    //lateinit var mHandler: Handler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewed = inflater.inflate(R.layout.fragment_manual, container, false)
        getItemSuhu()
        viewed.toggle_lamp.setOnClickListener {
            if(toggle_lamp.isChecked){
                myactivity.setCommand(f1,f2,f3,f4,"1.00")
//                manualPresenter.PostData(f1,f2,f3,f4,"1.00")
            }else{
                myactivity.setCommand(f1,f2,f3,f4,"0.00")
//                manualPresenter.PostData(f1,f2,f3,f4,"0.00")
            }
        }
        viewed.toggle_fan.setOnClickListener {
//            mHandler.removeCallbacks(refresher)
//            mHandler.removeCallbacksAndMessages(null)
            if(toggle_fan.isChecked){
                myactivity.setCommand(f1,f2,f3,"1.00",f5)
//                manualPresenter.PostData(f1,f2,f3,"1.00",f5)
            }else{
                myactivity.setCommand(f1,f2,f3,"0.00",f5)
//                manualPresenter.PostData(f1,f2,f3,"0.00",f5)
            }
        }

        viewed.auto_buttons.setOnClickListener {
            myactivity = activity as DrawerActivity
            myactivity.changeFragment("auto")
        }
        return viewed
    }

    @SuppressLint("SetTextI18n")
    fun getItemSuhu() {
        myactivity = activity as DrawerActivity
        val lastSuhu = myactivity.getFromActivity()

        viewed.tv_manualsuhu.text = lastSuhu.field_1.toString() + "\u00B0C"
        f1=lastSuhu.field_1.toString()
        f2=lastSuhu.field_2.toString()
        f3=lastSuhu.field_3.toString()
        f4=lastSuhu.field_4.toString()
        f5=lastSuhu.field_5.toString()

        val date : String = lastSuhu?.date_time!!
        val slicedDate1 : String = date.replace("T"," ")
        val slicedDate2 : String = slicedDate1.replace("Z","")
        val dateConvert = gmtFormat(slicedDate2)
        val formatDate = SimpleDateFormat("E, dd-MM-yyyy\nHH:mm:ss", Locale(slicedDate2))
        val formattedDate = formatDate.format(dateConvert)
        viewed.tv_manualdate.text = formattedDate

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

//        mHandler.postDelayed(refresher, 10000)
        //
        val fanbefore = viewed.stat_fan.drawable
        val lampbefore = viewed.stat_lamp.drawable
        val f1f=f1.toFloat()
        when {
            f1f in 25.0..30.0 -> {
                if (f2=="0"){
                    Glide.with(this@ManualFragment).load(R.drawable.normfanoff)
                        .apply(RequestOptions().placeholder(fanbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_fan)
                }else if (f2=="1"){
                    Glide.with(this@ManualFragment).load(R.drawable.normfanon)
                        .apply(RequestOptions().placeholder(fanbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_fan)
                }

                if (f3=="0"){
                    Glide.with(this@ManualFragment).load(R.drawable.normlampoff)
                        .apply(RequestOptions().placeholder(lampbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_lamp)
                }else if (f3=="1"){
                    Glide.with(this@ManualFragment).load(R.drawable.normlampon)
                        .apply(RequestOptions().placeholder(lampbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_lamp)
                }
            }
            f1f>30 -> {
                if (f2=="0"){
                    Glide.with(this@ManualFragment).load(R.drawable.hotfanoff)
                        .apply(RequestOptions().placeholder(fanbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_fan)
                }else if (f2=="1"){
                    Glide.with(this@ManualFragment).load(R.drawable.hotfanon)
                        .apply(RequestOptions().placeholder(fanbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_fan)
                }

                if (f3=="0"){
                    Glide.with(this@ManualFragment).load(R.drawable.hotlampoff)
                        .apply(RequestOptions().placeholder(lampbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_lamp)
                }else if (f3=="1"){
                    Glide.with(this@ManualFragment).load(R.drawable.hotlampon)
                        .apply(RequestOptions().placeholder(lampbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_lamp)
                }
            }
            f1f<25 -> {
                if (f2=="0"){
                    Glide.with(this@ManualFragment).load(R.drawable.coldfanoff)
                        .apply(RequestOptions().placeholder(fanbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_fan)
                }else if (f2=="1"){
                    Glide.with(this@ManualFragment).load(R.drawable.coldfanon)
                        .apply(RequestOptions().placeholder(fanbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_fan)
                }

                if (f3=="0"){
                    Glide.with(this@ManualFragment).load(R.drawable.coldlampoff)
                        .apply(RequestOptions().placeholder(lampbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_lamp)
                }else if (f3=="1"){
                    Glide.with(this@ManualFragment).load(R.drawable.coldlampon)
                        .apply(RequestOptions().placeholder(lampbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_lamp)
                }
            }
        }
        //////////////////

        when {
            f1f in 25.0..30.0 -> {
                viewed.auto_buttons.background=(resources.getDrawable(R.drawable.curved))
            }
            f1f>30 -> {
                viewed.auto_buttons.background=(resources.getDrawable(R.drawable.curvedhot))
            }
            f1f<25 -> {
                viewed.auto_buttons.background=(resources.getDrawable(R.drawable.curvedcold))
            }
        }
    }

    fun gmtFormat(dateP : String?) : Date?{
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale(dateP))
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val dateFormatted = "$dateP"
        return formatter.parse(dateFormatted)
    }
}
