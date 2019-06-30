package com.example.weather.WebServices

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface WeatherServiceApi {
    @GET("{key}/{latitude},{longitude}")
    fun getWeather(@Path("key") key: String, @Path("latitude") latitude: String,
                   @Path("longitude") longitude: String): Call<WeatherResult>
}