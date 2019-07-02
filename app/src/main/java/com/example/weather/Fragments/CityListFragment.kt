package com.example.weather.Fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.weather.Models.WeatherModels.WeatherModel
import com.example.weather.R
import com.example.weather.ViewModels.WeatherViewModel
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*
import kotlin.collections.ArrayList


class CityListFragment : Fragment() {


    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var cityListView: ListView

    private var weatherModelsObserver: Observer<List<WeatherModel>> = Observer {
        (this.cityListView.adapter as? CityWeatherListAdapater)?.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_city, container, false)

        cityListView = view.findViewById<ListView>(R.id.city_weather_list)
        cityListView.adapter = CityWeatherListAdapater(view.context)

        this.weatherViewModel.weatherModelsStatus.observe(this, this.weatherModelsObserver)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    private inner class CityWeatherListAdapater(context: Context): BaseAdapter() {

        private val mContext: Context

        init {
            this.mContext = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)

            val cityWeatherRow = layoutInflater.inflate(R.layout.city_weather_row, parent, false)

            val cityName = cityWeatherRow.findViewById<TextView>(R.id.city_row_weather_city)
            cityName.text = this@CityListFragment.weatherViewModel.getCityName(position)

            val cityTemperature = cityWeatherRow.findViewById<TextView>(R.id.city_row_weather_temperature)
            cityTemperature.text = this@CityListFragment.weatherViewModel.getCityCurrentTemp(position).toString()

            val cityIcon = cityWeatherRow.findViewById<ImageView>(R.id.city_row_weather_svg)
            cityIcon.setImageResource(this.getDrawableId(this@CityListFragment.weatherViewModel.getCityCurrentIcon(position)))

            return cityWeatherRow
        }

        override fun getItem(position: Int): Any {
            return "Placeholder string"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return this@CityListFragment.weatherViewModel.getCityCount()
        }

        fun getDrawableId(resourceName: String?) : Int {
            return resources.getIdentifier(resourceName, "id", this@CityListFragment.activity?.packageName)
        }
    }

    companion object {
        fun newInstance(vm: WeatherViewModel) : CityListFragment {
            val fragment = CityListFragment()
            fragment.weatherViewModel = vm
            return fragment
        }
    }
}
