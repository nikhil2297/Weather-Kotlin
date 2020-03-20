package com.weather.utilites.data

import org.json.JSONObject

data class ResponseData(
    val isStatus: Boolean,
    val successData: JSONObject,
    val responseCode: Int,
    val errorMessage: JSONObject
)