package com.example.weather

import retrofit2.http.GET

interface GeocoderServiceApi {
    @GET
    fun getLatLong()

    @GET
    fun getCity()
}