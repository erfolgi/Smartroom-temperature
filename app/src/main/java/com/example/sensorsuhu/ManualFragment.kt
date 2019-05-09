package com.example.sensorsuhu


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.sensorsuhu.model.Response
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

import kotlinx.android.synthetic.main.fragment_manual.*
import kotlinx.android.synthetic.main.fragment_manual.view.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class ManualFragment : Fragment() {

    //lateinit var manualPresenter: ManualPresenter
    lateinit var myactivity: DrawerActivity
//    lateinit var f1: String
//    lateinit var f2: String
//    lateinit var f3: String
//    lateinit var f4: String
//    lateinit var f5: String
    lateinit var viewed : View
    //lateinit var mHandler: Handler
    var first = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewed = inflater.inflate(R.layout.fragment_manual, container, false)
        //getItemSuhu()
        myactivity = activity as DrawerActivity
        val ref=myactivity.getDB()
        val postListener = object : ValueEventListener {

            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue(Response::class.java)
                // ...

                val suhuobj = post?.Suhu
                val kipasobj = post?.Kipas
                val lampuobj = post?.Lampu
                val suhu = suhuobj?.suhu!!.toFloat()
                val tgl=suhuobj.date
                // suhu_tx.text= suhu.toString()+"C"
                viewed.tv_manualsuhu.text = "$suhu\u00B0C"
                viewed.tv_manualdate.text=tgl



                if(first){
                    val kipas = kipasobj?.command
                    if (kipas==0){
                        viewed.toggle_fan.isChecked=false
                    }else if (kipas==1){
                        viewed.toggle_fan.isChecked=true
                    }
                    val lampu = lampuobj?.command
                    if (lampu==0){
                        viewed.toggle_lamp.isChecked=false
                    }else if (lampu==1){
                        viewed.toggle_lamp.isChecked=true
                    }

                    first=false
                }
                val statkipas = kipasobj?.status
                val statlampu = lampuobj?.status

                try {
                    setStatus(statkipas,statlampu,suhu)
                }catch (e:Exception){

                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("firing ", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)
        //////////////////////////////////////////////////////////////////////
        viewed.toggle_lamp.setOnClickListener {
            if(toggle_lamp.isChecked){

                myactivity.setCommandLampu(1)
//                manualPresenter.PostData(f1,f2,f3,f4,"1.00")
            }else{
                myactivity.setCommandLampu(0)
//                manualPresenter.PostData(f1,f2,f3,f4,"0.00")
            }
        }
        viewed.toggle_fan.setOnClickListener {
//            mHandler.removeCallbacks(refresher)
//            mHandler.removeCallbacksAndMessages(null)
            if(toggle_fan.isChecked){
                myactivity.setCommandKipas(1)
//                manualPresenter.PostData(f1,f2,f3,"1.00",f5)
            }else{
                myactivity.setCommandKipas(0)
//                manualPresenter.PostData(f1,f2,f3,"0.00",f5)
            }
        }

        viewed.auto_buttons.setOnClickListener {
            myactivity = activity as DrawerActivity
            myactivity.changeFragment("auto")
        }
        return viewed
    }

    fun setStatus(statkipas:Int? , statlampu:Int?, suhu:Float) {
        val fanbefore = viewed.stat_fan.drawable
        val lampbefore = viewed.stat_lamp.drawable
        Log.d("firing ", "Status Fragment"+suhu)

        when {
            suhu in 25.0..30.0 -> {
                Log.d("firing ", "Status Fragment Norm")
                if (statkipas==0){
                    Glide.with(this@ManualFragment).load(R.drawable.normfanoff)
                        .apply(RequestOptions().placeholder(fanbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_fan)
                }else if (statkipas==1){
                    Glide.with(this@ManualFragment).load(R.drawable.normfanon)
                        .apply(RequestOptions().placeholder(fanbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_fan)
                }

                if (statlampu==0){
                    Glide.with(this@ManualFragment).load(R.drawable.normlampoff)
                        .apply(RequestOptions().placeholder(lampbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_lamp)
                }else if (statlampu==1){
                    Glide.with(this@ManualFragment).load(R.drawable.normlampon)
                        .apply(RequestOptions().placeholder(lampbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_lamp)
                }
            }
            suhu>30 -> {
                Log.d("firing ", "Status Fragment Hot")
                if (statkipas==0){
                    Glide.with(this@ManualFragment).load(R.drawable.hotfanoff)
                        .apply(RequestOptions().placeholder(fanbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_fan)
                }else if (statkipas==1){
                    Glide.with(this@ManualFragment).load(R.drawable.hotfanon)
                        .apply(RequestOptions().placeholder(fanbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_fan)
                }

                if (statlampu==0){
                    Glide.with(this@ManualFragment).load(R.drawable.hotlampoff)
                        .apply(RequestOptions().placeholder(lampbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_lamp)
                }else if (statlampu==1){
                    Glide.with(this@ManualFragment).load(R.drawable.hotlampon)
                        .apply(RequestOptions().placeholder(lampbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_lamp)
                }
            }
            suhu<25 -> {
                Log.d("firing ", "Status Fragment Cold")
                if (statkipas==0){
                    Glide.with(this@ManualFragment).load(R.drawable.coldfanoff)
                        .apply(RequestOptions().placeholder(fanbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_fan)
                }else if (statkipas==1){
                    Glide.with(this@ManualFragment).load(R.drawable.coldfanon)
                        .apply(RequestOptions().placeholder(fanbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_fan)
                }

                if (statlampu==0){
                    Glide.with(this@ManualFragment).load(R.drawable.coldlampoff)
                        .apply(RequestOptions().placeholder(lampbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_lamp)
                }else if (statlampu==1){
                    Glide.with(this@ManualFragment).load(R.drawable.coldlampon)
                        .apply(RequestOptions().placeholder(lampbefore))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(viewed.stat_lamp)
                }
            }
        }
        //////////////////

        when {
            suhu in 25.0..30.0 -> {
                viewed.auto_buttons.background=(resources.getDrawable(R.drawable.curved))
            }
            suhu>30 -> {
                viewed.auto_buttons.background=(resources.getDrawable(R.drawable.curvedhot))
            }
            suhu<25 -> {
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
