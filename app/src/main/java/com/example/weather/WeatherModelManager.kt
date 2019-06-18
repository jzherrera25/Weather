package com.example.weather

import android.arch.lifecycle.LiveData
import android.util.Log
import com.example.weather.Models.WeatherModels.*
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.util.*

class WeatherModelManager {

    private val realm: Realm = Realm.getDefaultInstance()
    private val weatherModelResults: RealmResults<WeatherModel> = this.realm.where(WeatherModel::class.java).findAllAsync()
    private val weatherModelMap: MutableMap<Int, WeatherModel> = mutableMapOf()

    constructor() {
        // If no weather models in results. Add default city.
        if (this.realm.isEmpty) {
            this.realm.executeTransaction {
                val newWeatherModel = this.realm.createObject<WeatherModel>(DEFAULT_CITY)
                newWeatherModel.latitude = DEFAULT_CITY_LAT
                newWeatherModel.longitude = DEFAULT_CITY_LONG
                newWeatherModel.index = DEFAULT_CITY_INDEX
            }
        }


        // Find all weather models currently in database
        this.weatherModelResults.addChangeListener { element ->
            this@WeatherModelManager.addWeatherModelsToMap(element)
        }
    }

    fun getWeatherModelCount() = this.weatherModelMap.size

    fun getWeatherModel(index: Int): WeatherModel? {
        return this.weatherModelMap[index]
    }

    private fun addWeatherModelsToMap(element: RealmResults<WeatherModel>) {
        for (weatherModel in element) {
            Log.d("WeatherModelManager", weatherModel.city)
            Log.d("WeatherModelManager", weatherModel.index.toString())
            Log.d("WeatherModelManager", weatherModel.latitude.toString())
            Log.d("WeatherModelManager", weatherModel.longitude.toString())
            this.weatherModelMap.putIfAbsent(weatherModel.index!!, weatherModel)
        }
    }
}