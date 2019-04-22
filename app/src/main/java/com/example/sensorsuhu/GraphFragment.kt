package com.example.sensorsuhu


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlinx.android.synthetic.main.fragment_graph.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class GraphFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_graph, container, false)
        val myactivity = activity as DrawerActivity
        val lastSuhu = myactivity.getGraphs()
        view.graph.removeAllSeries()
        val series = LineGraphSeries<DataPoint>(
            arrayOf<DataPoint>(
                DataPoint(0.0, lastSuhu[0].field_1!!.toDouble()),
                DataPoint(1.0, lastSuhu[1].field_1!!.toDouble()),
                DataPoint(2.0, lastSuhu[2].field_1!!.toDouble()),
                DataPoint(3.0, lastSuhu[3].field_1!!.toDouble()),
                DataPoint(4.0, lastSuhu[4].field_1!!.toDouble())
            )
        )
        view.graph.addSeries(series)
        return view
    }
}
