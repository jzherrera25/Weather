package com.example.weather.Models.WeatherModels

import com.example.weather.WebServices.WeatherResult
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey

const val DEFAULT_CITY = "Orange"
const val DEFAULT_CITY_LAT = 33.7500378
const val DEFAULT_CITY_LONG = -117.8704931
const val DEFAULT_CITY_INDEX = 0

open class WeatherModel: RealmObject() {

    // Default city assignment.
    @PrimaryKey var city: String = DEFAULT_CITY
    var latitude: Double = DEFAULT_CITY_LAT
    var longitude: Double = DEFAULT_CITY_LONG

    var index: Int = DEFAULT_CITY_INDEX
    var lastUpdate: Long = 0
    var lastWeatherModel: RealmWeatherModel? = null

    @Ignore
    var weather: WeatherResult? = null
}