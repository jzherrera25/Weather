package com.example.weather

import com.example.weather.Models.WeatherModels.WeatherModel
import io.realm.Realm
import io.realm.kotlin.where

class WeatherModelManager {

    private val realm: Realm = Realm.getDefaultInstance()

    private val weatherModelList: MutableList<WeatherModel> = this.realm.where(WeatherModel::class.java).findAllAsync()
}