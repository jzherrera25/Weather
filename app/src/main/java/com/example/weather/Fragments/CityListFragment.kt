package com.example.weather.Fragments

import android.arch.lifecycle.Observer
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.weather.Models.WeatherModels.WeatherModel
import com.example.weather.R
import com.example.weather.ViewModels.WeatherViewModel
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*
import kotlin.collections.ArrayList


class CityListFragment : Fragment(), PlaceSelectionListener {

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var cityListView: ListView
    private lateinit var autocompleteSupportFragment: AutocompleteSupportFragment

    private var weatherModelsObserver: Observer<List<WeatherModel>> = Observer {
        (this.cityListView.adapter as? CityWeatherListAdapater)?.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_city, container, false)

        cityListView = view.findViewById(R.id.city_weather_list)
        cityListView.adapter = CityWeatherListAdapater(view.context)

        this.weatherViewModel.weatherModelsStatus.observe(this, this.weatherModelsObserver)


        if (!Places.isInitialized()) {
            Places.initialize(view.context, "AIzaSyCVxQr8GgMh4isXwOVeuDPdTWW_kMBfQYc")
        }

        this.autocompleteSupportFragment = this.fragmentManager?.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        this.autocompleteSupportFragment.setTypeFilter(TypeFilter.CITIES)
        this.autocompleteSupportFragment.setPlaceFields(listOf(Place.Field.LAT_LNG, Place.Field.NAME))
        this.autocompleteSupportFragment.setOnPlaceSelectedListener(this)

        return view
    }

    override fun onPlaceSelected(place: Place) {
        Log.d("WeatherCity", place.name)
        Log.d("WeatherCity", place.latLng.toString())
        Log.d("WeatherCity", "Lat: " + place.latLng?.latitude.toString())
        Log.d("WeatherCity", "Long: " + place.latLng?.longitude.toString())

        this.autocompleteSupportFragment.setText("")
        this.weatherViewModel.addCity(place?.name!!, place.latLng?.latitude!!, place.latLng?.longitude!!)
    }

    override fun onError(status: Status) {
        Log.d("WeatherCity", "poop")
    }

    private inner class CityWeatherListAdapater(context: Context): BaseAdapter(), View.OnLongClickListener, View.OnClickListener {
        private val mContext: Context = context

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)

            val cityWeatherRow = layoutInflater.inflate(R.layout.city_weather_row, parent, false)

            val cityName: TextView  = cityWeatherRow.findViewById(R.id.city_row_weather_city)
            cityName.text = this@CityListFragment.weatherViewModel.getCityName(position)

            val cityTemperature: TextView = cityWeatherRow.findViewById(R.id.city_row_weather_temperature)
            cityTemperature.text = this@CityListFragment.weatherViewModel.getCityCurrentTemp(position).toString()

            val cityIcon: ImageView = cityWeatherRow.findViewById(R.id.city_row_weather_svg)
            cityIcon.setImageResource(this.getDrawableId(this@CityListFragment.weatherViewModel.getCityCurrentIcon(position)))

            val deleteButton: ImageButton = cityWeatherRow.findViewById(R.id.city_row_delete)
            deleteButton.setOnClickListener {
                this@CityListFragment.weatherViewModel.removeCity(position)
            }

            cityWeatherRow.setOnLongClickListener(this)
            cityWeatherRow.setOnClickListener(this)

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

        override fun onLongClick(v: View?): Boolean {
            if (this.count > 1) {
                val deleteButton: ImageButton? = v?.findViewById(R.id.city_row_delete)
                deleteButton?.visibility = View.VISIBLE

                val cityTemperature: TextView? = v?.findViewById(R.id.city_row_weather_temperature)
                cityTemperature?.visibility = View.INVISIBLE
                return true
            }
            return false
        }

        override fun onClick(v: View?) {
            val deleteButton: ImageButton? = v?.findViewById(R.id.city_row_delete)

            val cityTemperature: TextView? = v?.findViewById(R.id.city_row_weather_temperature)

            deleteButton?.visibility = View.INVISIBLE
            cityTemperature?.visibility = View.VISIBLE

        }

        fun getDrawableId(resourceName: String?) : Int {
            return resources.getIdentifier(resourceName?.toString().orEmpty(), "drawable", this@CityListFragment.activity?.packageName)
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
