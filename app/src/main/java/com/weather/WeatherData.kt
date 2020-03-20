package com.weather

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("main") val mainData: MainData,
    @SerializedName("sys") val systemData: SystemData,
    @SerializedName("name") val cityName: String
)

data class MainData(
    @SerializedName("temp") val temp: Double,
    @SerializedName("pressure") val pressure: Long,
    @SerializedName("humidity") val humidity: Int
)

data class SystemData(
    @SerializedName("country") val country: String,
    @SerializedName("sunrise") val sunriseTime: Long,
    @SerializedName("sunset") val sunsetTime: Long
)

