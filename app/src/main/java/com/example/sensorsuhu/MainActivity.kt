package com.example.sensorsuhu

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var autoMode = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, ManualFragment(), ManualFragment::class.java.simpleName)
            .commit()

        auto_button.setOnClickListener {
            if(!autoMode){
                autoMode = true
                auto_button.text="Manual"
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, AutoFragment(), AutoFragment::class.java.simpleName)
                    .commit()
            }else{
                autoMode = false
                auto_button.text="Auto"
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, ManualFragment(), ManualFragment::class.java.simpleName)
                    .commit()
            }
        }
    }
}
