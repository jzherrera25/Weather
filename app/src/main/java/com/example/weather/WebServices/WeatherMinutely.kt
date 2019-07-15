package com.example.weather.WebServices

data class WeatherMinutely(val summary: String,
                           val icon: String,
                           val data: List<WeatherMinutelyData>)

data class WeatherMinutelyData(val time: Long,
                               val precipIntensity: Double,
                               val precipProbability: Double)