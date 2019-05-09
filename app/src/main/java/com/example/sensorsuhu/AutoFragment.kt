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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_auto.view.*
import kotlinx.android.synthetic.main.fragment_manual.view.*
import java.lang.Exception
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
    lateinit var ref:DatabaseReference
    lateinit var postListener:ValueEventListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewed = inflater.inflate(R.layout.fragment_auto, container, false)
        //
        //showSuhu()
        myactivity = activity as DrawerActivity
        ref=myactivity.getDB()
        postListener = object : ValueEventListener {

            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue(Response::class.java)
                // ...
                val suhuobj = post?.Suhu
                val suhu = suhuobj?.suhu!!.toFloat()
                val tgl=suhuobj.date
                viewed.tv_autosuhu.text = "$suhu\u00B0C"
                viewed.tv_autodate.text=tgl.toString()
                setStatus(suhu)


            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("firing ", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)



        viewed.manual_button.setOnClickListener {
            myactivity = activity as DrawerActivity
            myactivity.changeFragment("manual")
        }
        return viewed
    }

    override fun onDetach() {
        super.onDetach()
        ref.removeEventListener(postListener)
    }

    fun setStatus(suhu:Float) {
        when {
            suhu in 25.0..30.0 -> {
                viewed.autolayout.background=(resources.getDrawable(R.drawable.gradasi))
            }
            suhu>30 -> {
                viewed.autolayout.background=(resources.getDrawable(R.drawable.gradasihot))
            }
            suhu<25 -> {
                viewed.autolayout.background=(resources.getDrawable(R.drawable.gradasicold))
            }
        }

    }

}
