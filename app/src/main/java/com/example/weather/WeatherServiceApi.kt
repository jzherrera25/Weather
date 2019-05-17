package com.example.weather

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import javax.security.auth.callback.Callback


interface WeatherServiceApi {
    @GET("/{key}/{latitude},{longitude}?exclude={exclude}")
    fun getWeather(@Path("key") key: String, @Path("latitude") latitude: String,
                   @Path("longitude") longitude: String, @Path("exclude") exclude: String = ""): Call<WeatherModel>
}