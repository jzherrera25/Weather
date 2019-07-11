package com.example.weather

import android.util.Log
import com.example.weather.Models.WeatherModels.WeatherModel
import com.example.weather.WebServices.WeatherResult
import com.example.weather.WebServices.WeatherServiceApi
import io.reactivex.subjects.BehaviorSubject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherRepository private constructor() {

    private val darkSkyApiUrl: String = "https://api.darksky.net/forecast/"
    private val darkSkyApiKey: String = "582d4ce97f8bba2b75a40f6eba32a7dd"

    private val weatherServiceApi: WeatherServiceApi = Retrofit.Builder()
        .baseUrl(this.darkSkyApiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(WeatherServiceApi::class.java)

    private val weatherModelManager: WeatherModelManager = WeatherModelManager()

    private val weatherModelIndices = BehaviorSubject.create<List<Int>>()

    init {
        this.weatherModelManager.observeWeatherModels().subscribe {
            this.weatherModelIndices.onNext(it)
        }
    }

    fun observeWeatherModels() : BehaviorSubject<List<Int>> {
        return weatherModelIndices
    }

    fun doRefresh(index: Int) {
        this.getWeather(index)
    }

    fun removeCity(position: Int) {
        this.weatherModelManager.removeWeatherModel(position)
    }

    fun addCity(name: String, latitude: Double, longitude: Double) {
        this.weatherModelManager.addWeatherModel(name, latitude, longitude)
    }

    fun getWeatherModel(index: Int) : WeatherModel? {
        return this.weatherModelManager.getWeatherModel(index)
    }

    fun getCityCount() : Int {
        return this.weatherModelManager.getWeatherModelCount()
    }

    private fun getWeather(index: Int) {
        Log.d("WeatherRepository", "Weather Index " + index.toString())
        this.weatherModelManager.getWeatherModel(index)?.let {
            this.weatherServiceApi.getWeather(this.darkSkyApiKey, it.latitude.toString(), it.longitude.toString())
                .enqueue(object: Callback<WeatherResult> {
                    override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                        // Update weatherModelStatus
                        it.weather = response.body()
                        Log.d("WeatherRepository", it.weather?.currently?.temperature?.toString())

                        // Store weather in database.
                        this@WeatherRepository.weatherModelManager.updateWeatherModel(it)
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