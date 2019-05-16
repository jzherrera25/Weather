package com.example.weather

import retrofit2.Retrofit


class WeatherRepository {

    private val retrofit: Retrofit = Retrofit.Builder().build()

    companion object {
        val repository = WeatherRepository()
    }

}