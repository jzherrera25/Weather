package com.example.weather

import android.util.Log
import com.example.weather.Models.WeatherModels.*
import io.reactivex.subjects.BehaviorSubject
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.util.*

class WeatherModelManager {

    private val realm: Realm = Realm.getDefaultInstance()
    private val weatherModelResults: RealmResults<WeatherModel> = this.realm.where(WeatherModel::class.java).findAllAsync()
    private val weatherModelIndices = BehaviorSubject.create<List<Int>>()
    private val weatherModelMap: MutableMap<Int, WeatherModel> = mutableMapOf()

    init {
        // If no weather models in results. Add default city.
        if (this.realm.isEmpty) {
            this.addWeatherModel(DEFAULT_CITY, DEFAULT_CITY_LAT, DEFAULT_CITY_LONG)
        }

        // Find all weather models currently in database
        this.weatherModelResults.addChangeListener { element, changeSet ->

            var weatherIndexSet : MutableSet<Int> = mutableSetOf()

            changeSet.insertions.toCollection(weatherIndexSet)
            changeSet.deletions.toCollection(weatherIndexSet)
            changeSet.changes.toCollection(weatherIndexSet)

            var weatherIndices : List<Int>
            weatherIndices = when (this@WeatherModelManager.weatherModelMap.isEmpty()) {
                false -> {
                    weatherIndexSet.toList()
                }
                true -> {
                    element.map { it.index }.toList()
                }
            }

            this@WeatherModelManager.addWeatherModelsToMap(element)
            this@WeatherModelManager.weatherModelIndices.onNext(weatherIndices)
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
        var weatherModel = this.weatherModelMap[index]


        this.realm.executeTransaction {
            for(i in 0 until this.weatherModelMap.count()) {
                val tempWeatherModel = this.weatherModelMap[i]
                tempWeatherModel?.let {
                    if (it.index > weatherModel?.index!!) {
                        Log.d("WeatherModelManager", "Index " + it.index.toString())
                        it.index -= it.index - weatherModel?.index!!
                        Log.d("WeatherModelManager", "Index " + it.index.toString())
                        this.weatherModelMap[it.index] = it
                    }
                }
            }
            this.weatherModelMap.remove(this.weatherModelMap.count() - 1)
            weatherModel?.deleteFromRealm()
        }
    }

    fun addWeatherModel(name: String, latitude: Double, longitude: Double) {
        if (!this.weatherModelMap.values.map { it.city }.toList().contains(name)) {
            this.realm.executeTransaction {
                val newWeatherModel = this.realm.createObject<WeatherModel>(name)
                newWeatherModel.latitude = latitude
                newWeatherModel.longitude = longitude
                newWeatherModel.index = this.getWeatherModelCount()
            }
        }
    }

    fun observeWeatherModels() : BehaviorSubject<List<Int>> {
        return weatherModelIndices
    }

    fun getWeatherModelCount() = this.weatherModelMap.size

    fun getWeatherModel(index: Int): WeatherModel? {
        return this.weatherModelMap[index]
    }

    private fun addWeatherModelsToMap(element: RealmResults<WeatherModel>) {
        for (weatherModel in element) {
            this.weatherModelMap[weatherModel.index!!] = weatherModel
        }
    }
}