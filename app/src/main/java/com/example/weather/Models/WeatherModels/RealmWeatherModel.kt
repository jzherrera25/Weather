package com.example.weather.Models.WeatherModels

import com.example.weather.WebServices.WeatherResult
import io.realm.RealmList
import io.realm.RealmObject

open class RealmWeatherModel: RealmObject() {

    fun updateRealmWeatherModel(weatherResult: WeatherResult?) {
        this.currentSummary = weatherResult?.currently?.summary
        this.currentIcon = weatherResult?.currently?.icon
        this.currentTemp = weatherResult?.currently?.temperature?.toInt()

        this.hourlyTime = RealmList()
        this.hourlyIcon = RealmList()
        this.hourlyTemp = RealmList()
        weatherResult?.hourly?.data?.forEach {
            this.hourlyTime?.add(it.time)
            this.hourlyIcon?.add(it.icon)
            this.hourlyTemp?.add(it.temperature.toInt())
        }

        this.dailyTime = RealmList()
        this.dailyIcon = RealmList()
        this.dailyTempHigh = RealmList()
        this.dailyTempLow = RealmList()
        weatherResult?.daily?.data?.forEach {
            this.dailyTime?.add(it.time)
            this.dailyIcon?.add(it.icon)
            this.dailyTempHigh?.add(it.temperatureHigh.toInt())
            this.dailyTempLow?.add(it.temperatureLow.toInt())
        }
    }

    var currentSummary: String? = null
    var currentIcon: String? = null
    var currentTemp: Int? = null

    var hourlyTime: RealmList<Long>? = null
    var hourlyIcon: RealmList<String>? = null
    var hourlyTemp: RealmList<Int>? = null

    var dailyTime: RealmList<Long>? = null
    var dailyIcon: RealmList<String>? = null
    var dailyTempHigh: RealmList<Int>? = null
    var dailyTempLow: RealmList<Int>? = null
}
