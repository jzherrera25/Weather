package com.example.weather

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import io.realm.annotations.RealmField

open class WeatherModel: RealmObject() {

    @PrimaryKey var city: String = ""
    var latitude: String? = null
    var longitude: String? = null
}