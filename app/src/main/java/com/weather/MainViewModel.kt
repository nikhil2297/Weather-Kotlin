package com.weather

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.weather.utilites.CommunicationRouter
import com.weather.utilites.data.RequestData
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel() : ViewModel() {
    private val mutableJsonData = MutableLiveData<WeatherData>()
    private val errorData = MutableLiveData<String>()
    private val isLoading = MutableLiveData<Boolean>()

    private var getJob: Job? = null;

    fun isLoading(): LiveData<Boolean> {
        return isLoading
    }

    fun errorData(): LiveData<String> {
        return errorData
    }

    /**
     * 1 new things we are using here
     * 1. viewModelScope = Which is a kind of lifecycle for all the other class we have called
     *                     during this process
     * 2. if the viewModel gets destroyed. With the help of viewModelScope its destroy all the process
     *    which were called during this process
     */
    fun fetchWeatherData(context: Context): LiveData<WeatherData> {
        isLoading.value = true

        var communicationRouter =
            CommunicationRouter(context, getRequestData(), CONN_TYPE_HTTP)

        if (getJob != null) {
            getJob?.cancel()
        }

        getJob = viewModelScope.launch {

            communicationRouter.startCall(object : CommunicationRouter.BaseCallBack {
                override fun onResponse(jsonObject: JSONObject) {

                    isLoading.postValue(false)

                    if (jsonObject.has("data_new")) {
                        mutableJsonData.postValue(
                            Gson().fromJson(
                                jsonObject.get("data_new").toString(),
                                WeatherData::class.java
                            )
                        )
                    } else {
                        errorData.postValue(jsonObject.getString("error_data"))
                    }
                    Log.e("MainViewModel", "onResponse : $jsonObject")
                }
            });
        }

        return mutableJsonData;
    }

    fun getRequestData(): RequestData {
        var requestData = RequestData(
            null,
            getRequestBody(),
            "GET",
            "https://api.openweathermap.org/data/2.5/weather"
        )

        return requestData;
    }

    fun getRequestBody(): JSONObject {
        var bodyObject = JSONObject()

        bodyObject.put("q", "Mumbai,in")
        bodyObject.put("appid", "4ab446b577dffae1256808b44d7bade1")

        return bodyObject;
    }

    companion object {
        const val CONN_TYPE_HTTP = "conn.type.http";
    }
}