package com.example.weather.WebServices

import retrofit2.http.GET

interface GeocoderServiceApi {
    @GET
    fun getLatLong()
}