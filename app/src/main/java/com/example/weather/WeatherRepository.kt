package com.example.weather

import android.util.Log
import com.example.weather.Models.WeatherModels.WeatherInfoModel
import com.example.weather.Models.WeatherModels.WeatherModel
import com.example.weather.WebServices.GeocoderServiceApi
import com.example.weather.WebServices.WeatherResult
import com.example.weather.WebServices.WeatherServiceApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository private constructor() {

    private val darkSkyApiUrl: String = "https://api.darksky.net/forecast/"
    private val darkSkyApiKey: String = "582d4ce97f8bba2b75a40f6eba32a7dd"

    private val openCageGeocoderUrl: String = "https://api.opencagedata.com/geocode/v1/"
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

    fun doRefresh(index: Int) {
        this.getWeather(index)
    }

    fun findCity() {

    }

    fun getCityCount() : Int {
        return this.weatherModelManager.getWeatherModelCount()
    }

    private fun getWeather(index: Int) {
        this.weatherModelManager.getWeatherModel(index)?.let {
            this.weatherServiceApi.getWeather(this.darkSkyApiKey, it.latitude.toString(), it.longitude.toString())
                .enqueue(object: Callback<WeatherResult> {
                    override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                        Log.d("WeatherRepository", response.body()?.timezone.toString())
                        Log.d("WeatherRepository", response.body()?.currently?.temperature.toString())
                    }

                    override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                        Log.d("WeatherRepository", "Failed to get response.")
                    }
                })
        }
    }

    companion object {
        private var instance: WeatherRepository? = null
        fun getInstance(): WeatherRepository {
            synchronized(this) {
                if (this.instance == null) {
                    this.instance = WeatherRepository()
                }
                return this.instance!!
            }
        }
    }

}