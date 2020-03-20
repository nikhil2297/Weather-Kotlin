package com.weather.utilites

import android.content.Context
import android.util.Log
import com.weather.utilites.data.RequestData
import com.weather.utilites.data.ResponseData
import com.weather.utilites.okHttp.HttpConnector
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.json.JSONObject

class CommunicationRouter(
    private val context: Context,
    private val requestData: RequestData,
    private val connType: String,

    private val httpConnector: HttpConnector = HttpConnector()
) {

    companion object {
        const val ACTION_GET = "GET";
        const val CONN_TYPE_HTTP = "conn.type.http";
        val TAG = CommunicationRouter::class.java.simpleName;
    }

    /**
     * We check the connType then according to that we do the further operation
     * 1. if conn type is http then we cal the initHttp and we get the callback
     * 2. after receiving the data from httpcallback we pass it our callback. So that viewmodel can notify the ui
     */
    fun startCall(baseCallBack: BaseCallBack) {
        if (connType == CONN_TYPE_HTTP) {
            httpConnector.requestData = requestData;

            httpConnector.initHttp(object : HttpConnector.HttpCallBack {
                override fun successResponse(successResponse: JSONObject) {
                    baseCallBack.onResponse(successResponse)
                }

                override fun errorResponse(failedResponse: JSONObject) {
                    baseCallBack.onResponse(failedResponse)
                }
            })

        } else {
            Log.e(TAG, "startCall : " + "Unknown Find Type")
        }
    }

    interface BaseCallBack {
        fun onResponse(jsonObject: JSONObject)
    }
}