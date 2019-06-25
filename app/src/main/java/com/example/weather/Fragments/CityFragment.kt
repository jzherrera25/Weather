package com.example.weather.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView

import com.example.weather.R
import com.example.weather.WeatherRepository


class CityFragment : Fragment() {
    private val weatherRepository: WeatherRepository = WeatherRepository.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_city, container, false)

        val listView = view.findViewById<ListView>(R.id.city_weather_list)
        listView.adapter = CityWeatherListAdapater(view.context, this.weatherRepository)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    private inner class CityWeatherListAdapater(context: Context, weatherRepository: WeatherRepository): BaseAdapter() {

        private val mContext: Context
        private val weatherRepository: WeatherRepository

        init {
            this.mContext = context
            this.weatherRepository = weatherRepository
        }

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
            return this.weatherRepository.getCityCount()
        }
    }
}
