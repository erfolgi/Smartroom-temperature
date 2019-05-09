package com.example.sensorsuhu

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.sensorsuhu.model.Response

import com.google.firebase.database.*

import kotlinx.android.synthetic.main.activity_drawer.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_drawer.*
import java.lang.Exception

class DrawerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var autoMode = false

    lateinit var mHandler: Handler
    var first = true



    private lateinit var ref : DatabaseReference

    public fun getDB (): DatabaseReference {
        return ref
    }
    @SuppressLint("SetTextI18n")
    public fun changeFragment (name : String){
        if (name=="auto"){
            autoMode = true
            setCommandKipas(2)
            setCommandLampu(2)

            auto_button.text="Manual"
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.ascending_in,R.anim.ascending_out)
                .replace(R.id.fragment_container, AutoFragment(), AutoFragment::class.java.simpleName)
                .commit()
            auto_button.visibility= View.INVISIBLE
        }else if (name=="manual"){
            autoMode = false
            setCommandKipas(0)
            setCommandLampu(0)
            auto_button.text="Auto"
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.descending_in,R.anim.descending_out)
                .replace(R.id.fragment_container, ManualFragment(), ManualFragment::class.java.simpleName)
                .commit()
            auto_button.visibility= View.INVISIBLE
        }
    }

    public fun setCommandKipas (cmd:Int){
        ref.child("Kipas").child("command").setValue(cmd)
    }
    public fun setCommandLampu (cmd:Int){
        ref.child("Lampu").child("command").setValue(cmd)
    }
    @SuppressLint("SetTextI18n")
    fun setStatus(suhu:Float, cmdkipas:Int?, cmdlampu:Int?){
        val before=image_background.drawable
        when {
            suhu in 25.0..30.0 -> {

                try{
                    setTheme(R.style.AppTheme)
                    theme.applyStyle(R.style.AppTheme, true)
                }catch (e:Exception){
                    Log.d("firings", "Fail $e")
                }

                textStatus.text="NORMAL"
                Glide.with(this)
                    .load(R.drawable.normal).apply(
                        RequestOptions().placeholder(before))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(image_background)
                if (Build.VERSION.SDK_INT >= 21) {
                    window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
                }
                auto_button.background=(resources.getDrawable(R.drawable.curved))
            }
            suhu>30 -> {
                Log.d("firings", "Main HOT")
                setTheme(R.style.HotTheme)
                theme.applyStyle(R.style.HotTheme, true)
                textStatus.text="HOT"
                Glide.with(this).load(R.drawable.hot).apply(
                    RequestOptions().placeholder(before)).transition(DrawableTransitionOptions.withCrossFade()).into(image_background)
                if (Build.VERSION.SDK_INT >= 21) {
                    window.statusBarColor = resources.getColor(R.color.hotNav)
                }
                //auto_button.setBackgroundColor(resources.getColor(R.color.hotNav))
                auto_button.background=(resources.getDrawable(R.drawable.curvedhot))
            }
            suhu<25 -> {
                Log.d("firings", "Main Cold")
                setTheme(R.style.ColdTheme)
                theme.applyStyle(R.style.ColdTheme, true)
                textStatus.text="COLD"
                Glide.with(this).load(R.drawable.cold).apply(
                    RequestOptions().placeholder(before)).transition(DrawableTransitionOptions.withCrossFade()).into(image_background)
                if (Build.VERSION.SDK_INT >= 21) {
                    window.statusBarColor = resources.getColor(R.color.coldNav)
                }
                //auto_button.setBackgroundColor(resources.getColor(R.color.coldNav))
                auto_button.background=(resources.getDrawable(R.drawable.curvedcold))
            }
        }

        if(first){
            if (cmdkipas==2&&cmdlampu==2){
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
            first=false
        }

        // Toast.makeText(this, "update", Toast.LENGTH_SHORT).show()
    }
    private val refresher = Runnable {

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
//        val apiInterface : ApiInterface = ApiClient.getClient().create(ApiInterface::class.java)
//        val call : Call<SuhuResponse> = apiInterface.getLastDate()
//
//
//        this.mHandler = Handler()
//        this.mHandler.post(refresher)
        val database = FirebaseDatabase.getInstance()
        ref = FirebaseDatabase.getInstance().reference
        val postListener = object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                Log.d("firing ", "DataSnap: $dataSnapshot")
                val post = dataSnapshot.getValue(Response::class.java)

                Log.d("firing ", "Post: "+post.toString())
                val suhuobj = post!!.Suhu
                Log.d("firing ", "Suhu: "+suhuobj.toString())
                val suhu = suhuobj!!.suhu!!.toFloat()
                val kipasobj = post?.Kipas
                val kipas = kipasobj?.command
                val lampuobj = post?.Lampu
                val lampu = lampuobj?.command

                setStatus(suhu,kipas,lampu)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("firing ", "loadPost:onCancelled", databaseError.toException())
            }
        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        auto_button.setOnClickListener {

            if(!autoMode){
                autoMode = true
                setCommandKipas(2)
                setCommandLampu(2)
                auto_button.text="Manual"
                supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(R.anim.ascending_in,R.anim.ascending_out)
                    .replace(R.id.fragment_container, AutoFragment(), AutoFragment::class.java.simpleName)
                    .commit()
                auto_button.visibility= View.INVISIBLE
            }else{
                autoMode = false
                setCommandKipas(0)
                setCommandLampu(0)
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
                if(!autoMode){
                    setCommandKipas(2)
                    setCommandLampu(2)
                    changeFragment("auto")
                }

            }
            override fun onSwipeDown() {
                if(autoMode){
                    setCommandKipas(0)
                    setCommandLampu(0)
                changeFragment("manual")
                }
            }
        })
        ref.addValueEventListener(postListener)
    }
    override fun onResume() {
        super.onResume()


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
