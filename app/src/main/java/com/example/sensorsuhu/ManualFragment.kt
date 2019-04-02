package com.example.sensorsuhu


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.sensorsuhu.R
import kotlinx.android.synthetic.main.fragment_manual.*
import kotlinx.android.synthetic.main.fragment_manual.view.*


class ManualFragment : Fragment() {

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
        return view
    }


}
