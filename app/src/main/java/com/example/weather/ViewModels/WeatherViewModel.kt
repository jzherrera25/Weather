package com.example.weather.ViewModels

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.weather.Models.WeatherModels.WeatherModel
import com.example.weather.WeatherRepository
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel : ViewModel() {

    private val weatherRepository: WeatherRepository = WeatherRepository.getInstance()
    val weatherModelIndices: MutableLiveData<List<Int>> = MutableLiveData()

    init {
        this.weatherRepository.observeWeatherModels().subscribe {
            this.weatherModelIndices.value = it
            it.forEach { index ->
                this.doRefresh(index)
            }
        }
    }

    fun doRefreshAll() : Boolean {
        var didRefresh = false
        for (i in 0 until this.getCityCount()) {
            didRefresh = this.doRefresh(i)
        }
        return didRefresh
    }

    private fun doRefresh(position: Int) : Boolean {
        if (!this.useLastWeatherModel(position)) {
            this.weatherRepository.doRefresh(position)
            return true
        }
        return false
    }

    fun removeCity(position: Int) {
        this.weatherRepository.removeCity(position)
    }

    fun addCity(name: String, latitude: Double, longitude: Double) {
        this.weatherRepository.addCity(name, latitude, longitude)
    }

    private fun getCityLastUpdateTime(position: Int) : Long? {
        return this.weatherRepository.getWeatherModel(position)?.lastUpdate
    }

    private fun useLastWeatherModel(position: Int) : Boolean {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis
        var useLastWeatherModel = false
        this.getCityLastUpdateTime(position)?.let {
            useLastWeatherModel = ((currentTime - it) < 900000)
        }
        return useLastWeatherModel
    }

    fun getCityCurrentIcon(position: Int) : String? {
        val weatherModel = this.weatherRepository.getWeatherModel(position)
        return when(this.useLastWeatherModel(position)) {
            false -> (weatherModel?.weather?.currently?.icon?.replace('-', '_')).orEmpty()
            true -> (weatherModel?.lastWeatherModel?.currentIcon?.replace('-', '_')).orEmpty()
        }
    }

    fun getCityCurrentTemp(position: Int) : Int? {
        val weatherModel = this.weatherRepository.getWeatherModel(position)
        return when(this.useLastWeatherModel(position)) {
            false -> weatherModel?.weather?.currently?.temperature?.toInt()
            true -> weatherModel?.lastWeatherModel?.currentTemp
        }
    }

    fun getCityHourlyIcon(position: Int) : List<String>? {
        val weatherModel = this.weatherRepository.getWeatherModel(position)
        return when(this.useLastWeatherModel(position)) {
            false -> weatherModel?.weather?.hourly?.data?.map { it.icon.replace('-', '_') }?.toList()
            true -> weatherModel?.lastWeatherModel?.hourlyIcon?.map { it.replace('-', '_') }?.toList()
        }
    }

    fun getCityHourlyTemp(position: Int) : List<Int>? {
        val weatherModel = this.weatherRepository.getWeatherModel(position)
        return when(this.useLastWeatherModel(position)) {
            false -> weatherModel?.weather?.hourly?.data?.map { it.temperature.toInt() }?.toList()
            true -> weatherModel?.lastWeatherModel?.hourlyTemp?.toList()
        }
    }

    fun getCityHourlyTime(position: Int) : List<String>? {
        val weatherModel = this.weatherRepository.getWeatherModel(position)
        val timeFormat = SimpleDateFormat("h a")
        return when(this.useLastWeatherModel(position)) {
            false -> weatherModel?.weather?.hourly?.data?.map {
                val date = Date(it.time * 1000)
                timeFormat.format(date)
            }?.toList()
            true -> weatherModel?.lastWeatherModel?.hourlyTime?.map {
                val date = Date(it * 1000)
                timeFormat.format(date)
            }?.toList()
        }
    }

    fun getCityDailyIcon(position: Int) : List<String>? {
        val weatherModel = this.weatherRepository.getWeatherModel(position)
        return when(this.useLastWeatherModel(position)) {
            false -> weatherModel?.weather?.daily?.data?.map { it.icon.replace('-', '_') }?.toList()
            true -> weatherModel?.lastWeatherModel?.dailyIcon?.map { it.replace('-', '_') }?.toList()
        }
    }

    fun getCityDailyTempHigh(position: Int) : List<Int>? {
        val weatherModel = this.weatherRepository.getWeatherModel(position)
        return when(this.useLastWeatherModel(position)) {
            false -> weatherModel?.weather?.daily?.data?.map { it.temperatureHigh.toInt() }?.toList()
            true -> weatherModel?.lastWeatherModel?.dailyTempHigh?.toList()
        }
    }

    fun getCityDailyTempLow(position: Int) : List<Int>? {
        val weatherModel = this.weatherRepository.getWeatherModel(position)
        return when(this.useLastWeatherModel(position)) {
            false -> weatherModel?.weather?.daily?.data?.map { it.temperatureLow.toInt() }?.toList()
            true -> weatherModel?.lastWeatherModel?.dailyTempLow?.toList()
        }
    }

    fun getCityDailyDay(position: Int) : List<String>? {
        val weatherModel = this.weatherRepository.getWeatherModel(position)
        val calendar = Calendar.getInstance()
        return when(this.useLastWeatherModel(position)) {
            false -> weatherModel?.weather?.daily?.data?.map {
                val date = Date(it.time * 1000)
                calendar.time = date
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).toString()
            }?.toList()
            true -> weatherModel?.lastWeatherModel?.dailyTime?.map {
                val date = Date(it * 1000)
                calendar.time = date
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).toString()
            }?.toList()
        }
    }

    fun getCityWeatherDescription(position: Int) : String? {
        val weatherModel = this.weatherRepository.getWeatherModel(position)
        return when(this.useLastWeatherModel(position)) {
            false -> weatherModel?.weather?.currently?.summary
            true -> weatherModel?.lastWeatherModel?.currentSummary
        }
    }

    fun getCityName(position: Int) : String? {
        return this.weatherRepository.getWeatherModel(position)?.city
    }

    fun getCityCount() : Int {
        return this.weatherRepository.getCityCount()
    }

}