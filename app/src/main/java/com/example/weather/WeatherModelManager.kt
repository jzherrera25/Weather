package com.example.weather

import android.util.Log
import com.example.weather.Models.WeatherModels.*
import io.reactivex.subjects.BehaviorSubject
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.util.*

class WeatherModelManager {

    private val realm: Realm = Realm.getDefaultInstance()
    private val weatherModelResults: RealmResults<WeatherModel> = this.realm.where(WeatherModel::class.java).findAllAsync()
    private val weatherModelStatus = BehaviorSubject.create<List<WeatherModel>>()
    private val weatherModelMap: MutableMap<Int, WeatherModel> = mutableMapOf()

    init {
        // If no weather models in results. Add default city.
        if (this.realm.isEmpty) {
            this.addWeatherModel(DEFAULT_CITY, DEFAULT_CITY_LAT, DEFAULT_CITY_LONG)
        }


        // Find all weather models currently in database
        this.weatherModelResults.addChangeListener { element ->
            this@WeatherModelManager.addWeatherModelsToMap(element)
            this@WeatherModelManager.weatherModelStatus.onNext(this.weatherModelMap.toSortedMap().values.toList())
        }
    }

    fun updateWeatherModel(weatherModel: WeatherModel) {
        this.realm.executeTransaction {
            val calendar = Calendar.getInstance()
            weatherModel.lastUpdate = calendar.timeInMillis

            val realmWeatherModel = this.realm.createObject(RealmWeatherModel::class.java)
            realmWeatherModel.updateRealmWeatherModel(weatherModel?.weather)
            weatherModel.lastWeatherModel = realmWeatherModel
        }
    }

    fun removeWeatherModel(index: Int) {
        this.realm.executeTransaction {
            var weatherModel = this.weatherModelMap[index]
            this.weatherModelMap.remove(index)

            for(i in 0 until this.weatherModelMap.count()) {
                val tempWeatherModel = this.weatherModelMap[i]
                tempWeatherModel?.let {
                    if (it.index > weatherModel?.index!!) {
                        it.index -= it.index - weatherModel?.index!!
                        this.weatherModelMap.remove(i)
                        this.weatherModelMap[it.index] = it
                    }
                }
            }


            weatherModel?.deleteFromRealm()
        }
        this.weatherModelStatus.onNext(this.weatherModelMap.toSortedMap().values.toList())
    }

    fun addWeatherModel(name: String, latitude: Double, longitude: Double) {
        this.realm.executeTransaction {
            val newWeatherModel = this.realm.createObject<WeatherModel>(name)
            newWeatherModel.latitude = latitude
            newWeatherModel.longitude = longitude
            newWeatherModel.index = this.getWeatherModelCount()
        }
    }

    fun observeWeatherModels() : BehaviorSubject<List<WeatherModel>> {
        return weatherModelStatus
    }

    fun getWeatherModelCount() = this.weatherModelMap.size

    fun getWeatherModel(index: Int): WeatherModel? {
        return this.weatherModelMap[index]
    }

    fun getWeatherModels(): List<WeatherModel> {
        return this.weatherModelMap.toSortedMap().values.toList()
    }

    private fun addWeatherModelsToMap(element: RealmResults<WeatherModel>) {
        for (weatherModel in element) {
            this.weatherModelMap[weatherModel.index!!] = weatherModel
        }
    }
}