package com.example.weather.ViewModels

import android.arch.lifecycle.ViewModel
import com.example.weather.WeatherRepository

class CityFragmentViewModel : ViewModel() {

    private val weatherRepository: WeatherRepository

    init {
        this.weatherRepository = WeatherRepository.getInstance()
    }

    fun getCityCount(): Int = this.weatherRepository.getCityCount()

    fun getCityName(position: Int): String {
        return "Filler"
    }

    fun getCityWeatherDescription(position: Int): String {
        return "Test"
    }

    fun getCityTemperatureHigh(position:Int): Double {
        return 1.5
    }

    fun getCityTemperatureLow(position:Int): Double {
        return 1.toDouble()
    }

}