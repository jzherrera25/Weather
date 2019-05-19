package com.example.weather.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import com.example.weather.R
import com.example.weather.WeatherRepository

class WeatherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        val weatherRepository: WeatherRepository = WeatherRepository.getInstance()
        weatherRepository.doAction()

        val testButton = findViewById<Button>(R.id.doActionButton)
        testButton.setOnClickListener {
            weatherRepository.doAction()
        }
    }
}
