package com.example.weather.WebServices

import com.example.weather.Models.WeatherModels.WeatherCurrently
import com.example.weather.Models.WeatherModels.WeatherDaily
import com.example.weather.Models.WeatherModels.WeatherHourly
import com.example.weather.Models.WeatherModels.WeatherMinutely

data class WeatherResult(val latitude: String, val longitude: String, val timezone: String, val currently: WeatherCurrently?, val minutely: WeatherMinutely?, val hourly: WeatherHourly?, val daily: WeatherDaily?)
