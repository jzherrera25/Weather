package com.example.weather.ViewModels

import android.arch.lifecycle.ViewModel
import com.example.weather.WeatherRepository

class WeatherViewModel : ViewModel() {

    private val weatherRepository: WeatherRepository

    init {
        this.weatherRepository = WeatherRepository.getInstance()
    }

}