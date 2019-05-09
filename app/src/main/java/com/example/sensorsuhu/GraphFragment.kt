package com.example.sensorsuhu


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.DialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sensorsuhu.model.Response
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_graph.*

import kotlinx.android.synthetic.main.fragment_graph.view.*
import java.text.SimpleDateFormat
import java.util.*


class GraphFragment : DialogFragment() {
    lateinit var h1 : String
    lateinit var h2 : String
    lateinit var h3 : String
    lateinit var h4 : String
    lateinit var h5 : String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_graph, container, false)

        var myactivity = activity as DrawerActivity
        val ref=myactivity.getDB()
        val postListener = object : ValueEventListener {

            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val post = dataSnapshot.getValue(Response::class.java)
                // ...

                val historyobj = post?.History
                h1= historyobj!!.H1!!.suhu!!
                h2= historyobj!!.H2!!.suhu!!
                h3= historyobj!!.H3!!.suhu!!
                h4= historyobj!!.H4!!.suhu!!
                h5= historyobj!!.H5!!.suhu!!

                tv_h1.text=historyobj!!.H1!!.date!!.toString()
                tv_h2.text=historyobj!!.H2!!.date!!.toString()
                tv_h3.text=historyobj!!.H3!!.date!!.toString()
                tv_h4.text=historyobj!!.H4!!.date!!.toString()
                tv_h5.text=historyobj!!.H5!!.date!!.toString()


                //suhu = suhuobj?.suhu!!.toFloat()
                // suhu_tx.text= suhu.toString()+"C"
                //viewed.tv_manualsuhu.text = "$suhu\u00B0C"

                view.graph.removeAllSeries()
                val series = LineGraphSeries<DataPoint>(
                    arrayOf<DataPoint>(
                        DataPoint(0.0, h5.toDouble()),
                        DataPoint(1.0, h4.toDouble()),
                        DataPoint(2.0, h3.toDouble()),
                        DataPoint(3.0, h2.toDouble()),
                        DataPoint(4.0, h1.toDouble())
//                DataPoint(datearray[3], lastSuhu[3].field_1!!.toDouble())
//                DataPoint(datearray[4], lastSuhu[4].field_1!!.toDouble())
                    )
                )
                view.graph.addSeries(series)
               // view.graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(activity);
                view.graph.gridLabelRenderer.numHorizontalLabels = 5; // only 4 because of the space

// set manual x bounds to have nice steps
                view.graph.viewport.setMinX(0.0);
                view.graph.viewport.setMaxX(4.0);
                view.graph.viewport.isXAxisBoundsManual = true;

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
                //view.graph.gridLabelRenderer.setHumanRounding(false);

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("firing ", "loadPost:onCancelled", databaseError.toException())
            }
        }
        ref.addValueEventListener(postListener)
        return view
    }
    fun gmtFormat(dateP : String?) : Date{
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale(dateP))
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val dateFormatted = "$dateP"
        return formatter.parse(dateFormatted)
    }
}
