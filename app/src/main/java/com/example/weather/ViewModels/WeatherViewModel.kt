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
    private var didRefresh: Boolean = false
    val weatherModelsStatus: MutableLiveData<List<WeatherModel>> = MutableLiveData()

    init {
        this.weatherRepository.observeWeatherModels().subscribe {
            newWeatherModels -> this.weatherModelsStatus.value = newWeatherModels
        }
    }

    fun doRefresh() : Boolean {
        var refreshFlag = false

        val calendar = Calendar.getInstance()
        val currentTime = calendar.timeInMillis
        for (i in 1 until this.getCityCount()) {
            this.getCityLastUpdateTime(i)?.let {
                refreshFlag = refreshFlag || ((currentTime - it) > (900000))
            }
        }
        if (refreshFlag) {
            this.weatherRepository.doRefresh()
        }

        this.didRefresh = refreshFlag
        return refreshFlag
    }

    private fun getCityLastUpdateTime(position: Int) : Long? {
        return this.weatherModelsStatus.value?.get(position)?.lastUpdate
    }

    fun getCityCurrentIcon(position: Int) : String? {
        val weatherModel = this.weatherModelsStatus.value?.get(position)
        return when(this.didRefresh) {
            true -> weatherModel?.weather?.currently?.icon?.replace('-', '_')
            false -> weatherModel?.lastWeatherModel?.currentIcon?.replace('-', '_')
        }
    }

    fun getCityCurrentTemp(position: Int) : Int? {
        val weatherModel = this.weatherModelsStatus.value?.get(position)
        return when(this.didRefresh) {
            true -> weatherModel?.weather?.currently?.temperature?.toInt()
            false -> weatherModel?.lastWeatherModel?.currentTemp
        }
    }

    fun getCityHourlyIcon(position: Int) : List<String>? {
        val weatherModel = this.weatherModelsStatus.value?.get(position)
        return when(this.didRefresh) {
            true -> weatherModel?.weather?.hourly?.data?.map { it.icon?.replace('-', '_') }?.toList()
            false -> weatherModel?.lastWeatherModel?.hourlyIcon?.map { it.replace('-', '_') }?.toList()
        }
    }

    fun getCityHourlyTemp(position: Int) : List<Int>? {
        val weatherModel = this.weatherModelsStatus.value?.get(position)
        return when(this.didRefresh) {
            true -> weatherModel?.weather?.hourly?.data?.map { it.temperature.toInt() }?.toList()
            false -> weatherModel?.lastWeatherModel?.hourlyTemp?.toList()
        }
    }

    fun getCityHourlyTime(position: Int) : List<String>? {
        val weatherModel = this.weatherModelsStatus.value?.get(position)
        val timeFormat = SimpleDateFormat("h a")
        return when(this.didRefresh) {
            true -> weatherModel?.weather?.hourly?.data?.map {
                val date = Date(it.time * 1000)
                timeFormat.format(date)
            }?.toList()
            false -> weatherModel?.lastWeatherModel?.hourlyTime?.map {
                val date = Date(it * 1000)
                timeFormat.format(date)
            }?.toList()
        }
    }

    fun getCityDailyIcon(position: Int) : List<String>? {
        val weatherModel = this.weatherModelsStatus.value?.get(position)
        return when(this.didRefresh) {
            true -> weatherModel?.weather?.daily?.data?.map { it.icon?.replace('-', '_') }?.toList()
            false -> weatherModel?.lastWeatherModel?.dailyIcon?.map { it.replace('-', '_') }?.toList()
        }
    }

    fun getCityDailyTempHigh(position: Int) : List<Int>? {
        val weatherModel = this.weatherModelsStatus.value?.get(position)
        return when(this.didRefresh) {
            true -> weatherModel?.weather?.daily?.data?.map { it.temperatureHigh.toInt() }?.toList()
            false -> weatherModel?.lastWeatherModel?.dailyTempHigh?.toList()
        }
    }

    fun getCityDailyTempLow(position: Int) : List<Int>? {
        val weatherModel = this.weatherModelsStatus.value?.get(position)
        return when(this.didRefresh) {
            true -> weatherModel?.weather?.daily?.data?.map { it.temperatureLow.toInt() }?.toList()
            false -> weatherModel?.lastWeatherModel?.dailyTempLow?.toList()
        }
    }

    fun getCityDailyDay(position: Int) : List<String>? {
        val weatherModel = this.weatherModelsStatus.value?.get(position)
        val calendar = Calendar.getInstance()
        return when(this.didRefresh) {
            true -> weatherModel?.weather?.daily?.data?.map {
                val date = Date(it.time * 1000)
                calendar.time = date
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).toString()
            }?.toList()
            false -> weatherModel?.lastWeatherModel?.dailyTime?.map {
                val date = Date(it * 1000)
                calendar.time = date
                calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US).toString()
            }?.toList()
        }
    }

    fun getCityWeatherDescription(position: Int) : String? {
        val weatherModel = this.weatherModelsStatus.value?.get(position)
        return when(this.didRefresh) {
            true -> weatherModel?.weather?.currently?.summary
            false -> weatherModel?.lastWeatherModel?.currentSummary
        }
    }

    fun getCityName(position: Int) : String? {
        return this.weatherModelsStatus.value?.get(position)?.city
    }

    fun getCityCount() : Int {
        this.weatherModelsStatus.value?.let {
            return it.count()
        }
        return 0
    }

}