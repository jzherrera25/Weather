package com.example.weather.Models.WeatherModels

import io.realm.RealmObject

open class WeatherInfoModel: RealmObject() {
    var time: Long? = null
    var summary: String? = null
    var icon: String? = null
    var nearestStormDistance: Double? = null
    var nearestStormBearing: Double? = null
    var precipIntensity: Double? = null
    var precipProbability: Double? = null
    var temperature: Double? = null
    var apparentTemperature: Double? = null
    var dewPoint: Double? = null
    var humidity: Double? = null
    var pressure: Double? = null
    var windSpeed: Double? = null
    var windGust: Double? = null
    var cloudCover: Double? = null
    var uvIndex: Int? = null
    var visibility: Double? = null
    var ozone: Double? = null
}
