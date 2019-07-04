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
            this.realm.executeTransaction {
                val newWeatherModel = this.realm.createObject<WeatherModel>(DEFAULT_CITY)
                newWeatherModel.latitude = DEFAULT_CITY_LAT
                newWeatherModel.longitude = DEFAULT_CITY_LONG
                newWeatherModel.index = DEFAULT_CITY_INDEX
            }
            this.realm.executeTransaction {
                val newWeatherModel = this.realm.createObject<WeatherModel>("Placentia")
                newWeatherModel.latitude = 33.8714814
                newWeatherModel.longitude = -117.8617337
                newWeatherModel.index = 1
            }
        }


        // Find all weather models currently in database
        this.weatherModelResults.addChangeListener { element ->
            Log.d("WeatherMan", element.toString())
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
            var remove = this.weatherModelMap[index]

            this.weatherModelMap.values.forEach {
                if (it.index > remove?.index!!) {
                    it.index -= it.index - remove?.index!!
                }
            }

            remove?.deleteFromRealm()

            this.weatherModelMap.remove(index)
        }
        this.weatherModelStatus.onNext(this.weatherModelMap.toSortedMap().values.toList())
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