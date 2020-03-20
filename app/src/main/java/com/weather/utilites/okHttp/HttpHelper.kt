package com.weather.utilites.okHttp

import android.util.Log
import org.json.JSONObject
import java.lang.StringBuilder

class HttpHelper {
    val TAG = HttpHelper::class.java.simpleName;

    fun getParameterizedUrl(endPoint : String, parameters : JSONObject) : String{
        val urlBuilder : StringBuilder = StringBuilder();
        val keyIterator : Iterator<String> = parameters.keys()

        while (keyIterator.hasNext()){
            val keyName : String = keyIterator.next();

            if(urlBuilder.toString().isEmpty()){
                urlBuilder.append("?")
            }else{
                urlBuilder.append("&")
            }

            urlBuilder.append(keyName)
            urlBuilder.append("=")
            urlBuilder.append(parameters.get(keyName))
        }

        urlBuilder.insert(0, endPoint)

        Log.e(TAG, "getParameterizedUrl : ${urlBuilder.toString()}");

        return urlBuilder.toString();
    }
}