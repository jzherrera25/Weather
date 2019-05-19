package com.example.weather.Models.WeatherModels

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class WeatherModel: RealmObject() {

    // Default city assignment.
    @PrimaryKey var city: String = "Orange"
    var latitude: Double? = 33.7500378
    var longitude: Double? = -117.8704931

    var index: Int? = null

    var weatherInfoModel: WeatherInfoModel = WeatherInfoModel()
    var hourlyWeatherInfoModel: RealmList<WeatherInfoModel> = RealmList()
    var dailyWeatherInfoModel: RealmList<WeatherInfoModel> = RealmList()
}