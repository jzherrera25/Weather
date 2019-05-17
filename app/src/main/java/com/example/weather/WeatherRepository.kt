package com.example.weather

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository {

    private val darkSkyApiUrl: String = "https://api.darksky.net/forecast"
    private val darkSkyApiKey: String = "582d4ce97f8bba2b75a40f6eba32a7dd"

    private val openCageGeocoderUrl: String = "https://api.opencagedata.com/geocode/v1"
    private val openCageGeocoderApiKey: String = "b5c01f1e113848e895577e058f5ddbc4"

    private val weatherServiceApi: WeatherServiceApi = Retrofit.Builder()
        .baseUrl(this.darkSkyApiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherServiceApi::class.java)

    private val geocoderServiceApi: GeocoderServiceApi = Retrofit.Builder()
        .baseUrl(this.openCageGeocoderUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(GeocoderServiceApi::class.java)

    private val weatherModelManager: WeatherModelManager = WeatherModelManager()

    fun getWeather() {
        this.weatherServiceApi.getWeather(this.darkSkyApiKey, "0", "0")
            .enqueue(object: Callback<WeatherModel> {
                override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            })
    }

    companion object {
        val repository = WeatherRepository()
    }

}