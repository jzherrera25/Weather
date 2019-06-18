package com.example.weather.WebServices

import com.example.weather.Models.WeatherModels.WeatherInfoModel
import com.example.weather.Models.WeatherModels.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface WeatherServiceApi {
    @GET("{key}/{latitude},{longitude}")
    fun getWeather(@Path("key") key: String, @Path("latitude") latitude: String,
                   @Path("longitude") longitude: String): Call<WeatherResult>
}