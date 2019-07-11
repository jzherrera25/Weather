package com.example.weather

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmSchema

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
//        Realm.deleteRealm(Realm.getDefaultConfiguration())
    }
}