package com.example.weather.WebServices

import com.example.weather.Models.WeatherModels.WeatherCurrently

data class WeatherResult(val latitude: String, val longitude: String, val timezone: String, val currently: WeatherCurrently?, val minutely: WeatherMinutely?, val hourly: WeatherHourly?, val daily: WeatherDaily?)
