package com.example.weather

import android.app.Application
import io.realm.Realm

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
    }
}