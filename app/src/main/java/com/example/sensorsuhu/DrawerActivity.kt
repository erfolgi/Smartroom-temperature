package com.example.sensorsuhu

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.sensorsuhu.api.ApiClient
import com.example.sensorsuhu.api.ApiInterface
import com.example.sensorsuhu.model.SuhuModel
import com.example.sensorsuhu.model.SuhuResponse
import com.example.sensorsuhu.presenter.ManualPresenter
import com.example.sensorsuhu.view.ManualView
import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_drawer.*
import retrofit2.Call

class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ManualView {
    lateinit var manualPresenter: ManualPresenter
    var autoMode = false
    lateinit var listData: SuhuModel
    lateinit var mHandler: Handler
    var f1 : Float = 0.0F
    lateinit var f2 : String
    lateinit var f3 : String
    lateinit var f4 : String
    lateinit var f5 : String
    lateinit var listGraph : ArrayList<SuhuModel>

    override fun showItemSuhu(listSuhu: ArrayList<SuhuModel>) {
        listGraph = listSuhu
        val listSuhu = listSuhu.last()
        listData = listSuhu

        f1= listSuhu.field_1!!.toFloat()
        f2 =listSuhu.field_2.toString()
        f3=listSuhu.field_3.toString()
        f4=listSuhu.field_4.toString()
        f5=listSuhu.field_5.toString()

        val before=image_background.drawable
        when {
            f1 in 25.0..30.0 -> {
                textStatus.text="NORMAL"
                Glide.with(this)
                    .load(R.drawable.normal).apply(
                        RequestOptions().placeholder(before))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(image_background)
                setTheme(R.style.AppTheme)
                if (Build.VERSION.SDK_INT >= 21) {
                    window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
                }
                auto_button.background=(resources.getDrawable(R.drawable.curved))
            }
            f1>30 -> {
                textStatus.text="HOT"
                Glide.with(this).load(R.drawable.hot).apply(
                    RequestOptions().placeholder(before)).transition(DrawableTransitionOptions.withCrossFade()).into(image_background)
                setTheme(R.style.HotTheme)
                if (Build.VERSION.SDK_INT >= 21) {
                    window.statusBarColor = resources.getColor(R.color.hotNav)
                }
                //auto_button.setBackgroundColor(resources.getColor(R.color.hotNav))
                auto_button.background=(resources.getDrawable(R.drawable.curvedhot))
            }
            f1<25 -> {
                textStatus.text="COLD"
                Glide.with(this).load(R.drawable.cold).apply(
                    RequestOptions().placeholder(before)).transition(DrawableTransitionOptions.withCrossFade()).into(image_background)
                setTheme(R.style.ColdTheme)
                if (Build.VERSION.SDK_INT >= 21) {
                    window.statusBarColor = resources.getColor(R.color.coldNav)
                }
                //auto_button.setBackgroundColor(resources.getColor(R.color.coldNav))
                auto_button.background=(resources.getDrawable(R.drawable.curvedcold))
            }
        }

        if (f4=="2"&&f5=="2"){

            auto_button.text="Manual"
            auto_button.visibility= View.INVISIBLE
            autoMode = true
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, AutoFragment(), AutoFragment::class.java.simpleName)
                .commit()
        } else {
            auto_button.visibility= View.INVISIBLE
            auto_button.text="Auto"
            autoMode = false
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, ManualFragment(), AutoFragment::class.java.simpleName)
                .commit()
        }
        Toast.makeText(this,"Refreshed", Toast.LENGTH_SHORT).show()
        mHandler.postDelayed(refresher, 10000)
    }

    public fun getFromActivity (): SuhuModel {
        return listData
    }
    public fun getGraphs (): ArrayList<SuhuModel> {
        return listGraph
    }
    public fun changeFragment (name : String){
        if (name=="auto"){
            autoMode = true
            setCommand (f1.toString(),f2,f3,"2","2")
            auto_button.text="Manual"
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.ascending_in,R.anim.ascending_out)
                .replace(R.id.fragment_container, AutoFragment(), AutoFragment::class.java.simpleName)
                .commit()
            auto_button.visibility= View.INVISIBLE
        }else if (name=="manual"){
            autoMode = false
            setCommand (f1.toString(),f2,f3,"0","0")
            auto_button.text="Auto"
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.descending_in,R.anim.descending_out)
                .replace(R.id.fragment_container, ManualFragment(), ManualFragment::class.java.simpleName)
                .commit()
            auto_button.visibility= View.INVISIBLE
        }
    }

    public fun setCommand (f1: String,f2: String,f3: String,f4: String,f5: String){
        manualPresenter.cancelCall()
        mHandler.removeCallbacks(refresher)
        mHandler.removeCallbacksAndMessages(null)
        manualPresenter.PostData(f1,f2,f3,f4,f5)
    }
    private val refresher = Runnable {
        manualPresenter.getManualSuhuItem()
        //Toast.makeText(this@MainActivity,"Delay", LENGTH_SHORT).show()
    }//runnable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer)
        setSupportActionBar(toolbar)
        supportActionBar!!.title=""

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        //////// Use This
        val apiInterface : ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
        val call : Call<SuhuResponse> = apiInterface.getSuhuItem()
        manualPresenter = ManualPresenter(call, this, this)

        this.mHandler = Handler()
        this.mHandler.post(refresher)

        auto_button.setOnClickListener {
            manualPresenter.cancelCall()
            if(!autoMode){
                autoMode = true
                setCommand (f1.toString(),f2,f3,"2","2")
                auto_button.text="Manual"
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.ascending_in,R.anim.ascending_out)
                    .replace(R.id.fragment_container, AutoFragment(), AutoFragment::class.java.simpleName)
                    .commit()
                auto_button.visibility= View.INVISIBLE
            }else{
                autoMode = false
                setCommand (f1.toString(),f2,f3,"0","0")
                auto_button.text="Auto"
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.descending_in,R.anim.descending_out)
                    .replace(R.id.fragment_container, ManualFragment(), ManualFragment::class.java.simpleName)
                    .commit()
                auto_button.visibility= View.INVISIBLE
            }

        }
        var l = OnSwipeTouchListener(this)
        //fragment_container.setOnTouchListener

        fragment_container.setOnTouchListener(object: OnSwipeTouchListener(this){
            override fun onSwipeUp() {
                if(autoMode == false){
                    changeFragment("auto")
                }

            }
            override fun onSwipeDown() {
                if(autoMode == true){
                changeFragment("manual")
                }
            }
        })
    }
    override fun onResume() {
        super.onResume()
        mHandler.removeCallbacks(refresher)
        mHandler.removeCallbacksAndMessages(null)
        this.mHandler.post(refresher)

    }
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.drawer, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_graph -> {
                val mGraphFragment = GraphFragment()
                val mFragmentManager = supportFragmentManager
                mGraphFragment.show(mFragmentManager, GraphFragment::class.java!!.simpleName)
            }
            R.id.nav_about -> {
                val mAboutFragment = AboutFragment()
                val mFragmentManager = supportFragmentManager
                mAboutFragment.show(mFragmentManager, AboutFragment::class.java!!.simpleName)
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
