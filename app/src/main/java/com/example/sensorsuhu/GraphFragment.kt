package com.example.sensorsuhu


import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_graph.view.*
import kotlinx.android.synthetic.main.fragment_manual.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class GraphFragment : DialogFragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_graph, container, false)

        var datearray: ArrayList<Date> = ArrayList(5)

        val myactivity = activity as DrawerActivity
        val lastSuhu = myactivity.getGraphs()

        for (i in 0..4){
            val date : String = lastSuhu[i].date_time!!
            val slicedDate1 : String = date.replace("T"," ")
            val slicedDate2 : String = slicedDate1.replace("Z","")
            val dateConvert:Date = gmtFormat(slicedDate2)
            val formatDate = SimpleDateFormat("HH.mm", Locale(slicedDate2))
            val formattedDate= formatDate.format(dateConvert)
            val doubledate=formattedDate.toDouble()
            datearray.add(i,dateConvert)

            //[i]=dateConvert

        }
        Log.d("updated",datearray.toString())
        Log.d("updated",lastSuhu[2].field_1!!.toDouble().toString())

        view.graph.removeAllSeries()
        val series = LineGraphSeries<DataPoint>(
            arrayOf<DataPoint>(
                DataPoint(datearray[0], lastSuhu[0].field_1!!.toDouble()),
                DataPoint(datearray[1], lastSuhu[1].field_1!!.toDouble()),
                DataPoint(datearray[2], lastSuhu[2].field_1!!.toDouble())
//                DataPoint(datearray[3], lastSuhu[3].field_1!!.toDouble())
//                DataPoint(datearray[4], lastSuhu[4].field_1!!.toDouble())
            )
        )
        view.graph.addSeries(series)
        view.graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(activity);
        view.graph.gridLabelRenderer.numHorizontalLabels = 3; // only 4 because of the space

// set manual x bounds to have nice steps
        view.graph.viewport.setMinX(datearray[0].time.toDouble());
        view.graph.viewport.setMaxX(datearray[2].time.toDouble());
        view.graph.viewport.isXAxisBoundsManual = true;

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        view.graph.gridLabelRenderer.setHumanRounding(false);

        return view
    }
    fun gmtFormat(dateP : String?) : Date{
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale(dateP))
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val dateFormatted = "$dateP"
        return formatter.parse(dateFormatted)
    }
}
