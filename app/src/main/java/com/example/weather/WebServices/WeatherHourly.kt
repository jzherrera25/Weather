package com.example.weather.WebServices

data class WeatherHourly(val summary: String,
                         val icon: String,
                         val data: List<WeatherHourlyData>)

data class WeatherHourlyData(val time: Long,
                             val summary: String,
                             val icon: String,
                             val precipIntensity: Double,
                             val precipProbability: Double,
                             val temperature: Double,
                             val apparentTemperature: Double,
                             val dewPoint: Double,
                             val humidity: Double,
                             val pressure: Double,
                             val windSpeed: Double,
                             val windGust: Double,
                             val windBearing: Double,
                             val cloudCover: Double,
                             val uvIndex: Int,
                             val visibility: Double,
                             val ozone: Double)