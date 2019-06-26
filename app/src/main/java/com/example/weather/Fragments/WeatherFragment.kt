package com.example.weather.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView

import com.example.weather.R
import com.example.weather.ViewModels.WeatherViewModel
import kotlinx.android.synthetic.main.hourly_weather_col.view.*

class WeatherFragment : Fragment() {

    var position: Int = 0
    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.weather_fragment_hourly_recycler_view)
        recyclerView.adapter = HourlyRecyclerAdapter(view.context)

        val listView = view.findViewById<ListView>(R.id.weather_fragment_daily_list_view)
        listView.adapter = DailyListAdapter(view.context)

        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    private open inner class HourlyViewHolder(v: View): RecyclerView.ViewHolder(v) {

    }

    private inner class HourlyRecyclerAdapter(context: Context) : RecyclerView.Adapter<HourlyViewHolder>() {
        private val mContext: Context = context

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyViewHolder {
            val layoutInflater = LayoutInflater.from(this.mContext)
            val hourlyWeatherCol = layoutInflater.inflate(R.layout.hourly_weather_col, parent, false)
            return HourlyViewHolder(hourlyWeatherCol)
        }

        override fun getItemCount(): Int {
            return 24
        }

        override fun onBindViewHolder(holder: HourlyViewHolder, position: Int) {
            holder.itemView.daily_weather_hour.text = "1pm"
        }

    }

    private inner class DailyListAdapter(context: Context) : BaseAdapter() {
        private val mContext: Context = context

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)

            val cityWeatherRow = layoutInflater.inflate(R.layout.city_weather_row, parent, false)
            val cityName = cityWeatherRow.findViewById<TextView>(R.id.city_row_weather_city)

            return cityWeatherRow
        }

        override fun getItem(position: Int): Any {
            return "Placeholder string"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return 10
        }
    }

    companion object {
        fun newInstance(position: Int, vm: WeatherViewModel) : WeatherFragment {
            val fragment = WeatherFragment()
            fragment.position = position
            fragment.weatherViewModel = vm
            return fragment
        }
    }
}
