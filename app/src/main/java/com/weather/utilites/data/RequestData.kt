package com.weather.utilites.data

import org.json.JSONObject

data class RequestData(
    val requestHeader: JSONObject?,
    val requestBody: JSONObject,
    val requestType: String,
    var requestEndPoint: String
)