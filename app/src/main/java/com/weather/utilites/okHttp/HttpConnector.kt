package com.weather.utilites.okHttp;

import android.util.Log
import com.weather.utilites.data.RequestData
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.IOException

class HttpConnector(
    private val builder: OkHttpClient.Builder = OkHttpClient.Builder(),
    private val httpHelper: HttpHelper = HttpHelper()
) {
    private val TAG: String = HttpConnector::class.java.simpleName;

    lateinit var requestData: RequestData;

    fun initHttp(httpCallBack: HttpCallBack) {
        attachLoggingInterceptor(builder)

        var url: String = "";

        val requestBuilder: Request.Builder = Request.Builder();

        /**
         * We check method type in this and if method type
         * 1. If it is GET then we fetch the request body and add all its element in its parameter
         * 2. And create parameterized Url
         */
        if (requestData.requestType.equals("GET")) {
            url =
                httpHelper.getParameterizedUrl(requestData.requestEndPoint, requestData.requestBody)

            Log.e(TAG, "Parameterized Url $url");
        }

        requestBuilder.url(url);

        //We create request header and added it to request Builder
        if (requestData.requestHeader != null) {
            addHeaders(requestBuilder)
        }

        //We perfrom http operation
        doHttpCall(requestBuilder, httpCallBack)
    }

    /**
     * 1. We create a network call to given endpoint
     * 2. accroding to okHttp callback we update the response in the responseObject
     * 3. If its a onResponse we call our callback onSuccess and responseObject with it
     * 4. If its a onFailure we call our callback onFailure and responseObject with it
     */
    private fun doHttpCall(requestBuilder: Request.Builder, httpCallBack: HttpCallBack) {
        var responseObject = JSONObject()

        builder.build().newCall(requestBuilder.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, e.message.toString());

                responseObject.put("error_data", e.message)

                httpCallBack.errorResponse(responseObject)
            }

            override fun onResponse(call: Call, response: Response) {
                val responseStr = response.body?.string()

                Log.e(TAG, responseStr.toString());

                responseObject.put("data_new", responseStr)

                httpCallBack.successResponse(responseObject)
            }
        })
    }

    private fun addHeaders(requestBuilder: Request.Builder) {
        val headerObject = requestData.requestHeader;

        var keys: Iterator<String> = requestData.requestHeader!!.keys();

        while (keys.hasNext()) {
            val keyName: String = keys.next()
            if (headerObject != null) {
                requestBuilder.addHeader(keyName, headerObject.getString(keyName))
            }
        }

        Log.e(TAG, "Headers : ${headerObject.toString()}")
    }

    private fun attachLoggingInterceptor(builder: OkHttpClient.Builder) {
        // Logging interceptor
        val httpLogging = HttpLoggingInterceptor()
        httpLogging.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(httpLogging)
    }

    interface HttpCallBack {
        fun successResponse(successResponse: JSONObject)
        fun errorResponse(failedResponse: JSONObject)
    }
}