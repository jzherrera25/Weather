package com.example.weather.Fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.example.weather.Models.WeatherModels.WeatherModel

import com.example.weather.R
import com.example.weather.ViewModels.WeatherViewModel
import kotlinx.android.synthetic.main.hourly_weather_col.view.*

class WeatherFragment : Fragment() {

    var position: Int = 0
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var cityTextView: TextView
    private lateinit var currentDescriptionTextView: TextView
    private lateinit var currentTempTextView: TextView
    private lateinit var hourlyRecyclerView: RecyclerView
    private lateinit var dailyListView: ListView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private var weatherModelsObserver: Observer<List<WeatherModel>> = Observer { newWeatherModels ->
        this.cityTextView?.text = this.weatherViewModel.getCityName(position)

        this.currentDescriptionTextView?.text = this.weatherViewModel.getCityWeatherDescription(position)

        this.currentTempTextView?.text = this.weatherViewModel.getCityCurrentTemp(position).toString()

        this.hourlyRecyclerView?.adapter?.notifyDataSetChanged()

        (this.dailyListView?.adapter as? DailyListAdapter)?.notifyDataSetChanged()

        this.swipeRefreshLayout.isRefreshing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.weatherViewModel.weatherModelsStatus.observe(this, this.weatherModelsObserver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        hourlyRecyclerView = view.findViewById<RecyclerView>(R.id.weather_fragment_hourly_recycler_view)
        hourlyRecyclerView.adapter = HourlyRecyclerAdapter(view.context)

        dailyListView = view.findViewById<ListView>(R.id.weather_fragment_daily_list_view)
        dailyListView.adapter = DailyListAdapter(view.context)

        this.cityTextView = view.findViewById(R.id.weather_fragment_city)

        this.currentDescriptionTextView = view.findViewById(R.id.weather_fragment_description)

        this.currentTempTextView = view.findViewById(R.id.weather_fragment_temperature)

        this.swipeRefreshLayout = view.findViewById(R.id.weather_swipe_refresh)
        this.swipeRefreshLayout.setOnRefreshListener {
            this.swipeRefreshLayout.isRefreshing = this.weatherViewModel.doRefresh()
        }

        return view
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
            holder.itemView.hourly_weather_hour.text = this@WeatherFragment.weatherViewModel.getCityHourlyTime(this@WeatherFragment.position)?.get(position)

            holder.itemView.hourly_weather_temperature.text = this@WeatherFragment.weatherViewModel.getCityHourlyTemp(this@WeatherFragment.position)?.get(position).toString()

            holder.itemView.hourly_weather_svg.setImageResource(this.getDrawableId(this@WeatherFragment.weatherViewModel.getCityHourlyIcon(this@WeatherFragment.position)?.get(position)))
        }

        fun getDrawableId(resourceName: String?) : Int {
            return resources.getIdentifier(resourceName, "id", this@WeatherFragment.activity?.packageName)
        }
    }

    private inner class DailyListAdapter(context: Context) : BaseAdapter() {
        private val mContext: Context = context

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)

            val cityWeatherRow = layoutInflater.inflate(R.layout.city_weather_row, parent, false)

            val dayTextView = cityWeatherRow.findViewById<TextView>(R.id.city_row_weather_city)
            dayTextView.text = this@WeatherFragment.weatherViewModel.getCityDailyDay(this@WeatherFragment.position)?.get(position).toString()

            val temperatureTextView = cityWeatherRow.findViewById<TextView>(R.id.city_row_weather_temperature)
            temperatureTextView.text = this@WeatherFragment.weatherViewModel.getCityDailyTempHigh(this@WeatherFragment.position)?.get(position).toString() + " " +
                    this@WeatherFragment.weatherViewModel.getCityDailyTempLow(this@WeatherFragment.position)?.get(position).toString()

            val iconImageView = cityWeatherRow.findViewById<ImageView>(R.id.city_row_weather_svg)
            iconImageView.setImageResource(this.getDrawableId(this@WeatherFragment.weatherViewModel.getCityDailyIcon(this@WeatherFragment.position)?.get(position)))

            return cityWeatherRow
        }

        override fun getItem(position: Int): Any {
            return "Placeholder string"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return 8
        }

        fun getDrawableId(resourceName: String?) : Int {
            return resources.getIdentifier(resourceName, "id", this@WeatherFragment.activity?.packageName)
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
