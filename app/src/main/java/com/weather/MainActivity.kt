package com.weather

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    lateinit var mainViewModel: MainViewModel

    lateinit var tvLocation: TextView
    lateinit var tvTime: TextView
    lateinit var tvTemp: TextView

    lateinit var ivTempIcon: ImageView

    lateinit var layoutLoading: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvLocation = findViewById(R.id.tv_location)
        tvTime = findViewById(R.id.tv_time)
        tvTemp = findViewById(R.id.tv_temp)

        ivTempIcon = findViewById(R.id.imageView)

        layoutLoading = findViewById(R.id.layout_loading)

        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        getWeatherData()
    }

    private fun getWeatherData() {
        mainViewModel.fetchWeatherData(this).observe(this,
            Observer<WeatherData> {
                if (it != null) {

                    Log.e(TAG, it.cityName)

                    processWeather(it)
                }
            })

        mainViewModel.errorData().observe(this, Observer {
            tvLocation.text = it;
        })

        mainViewModel.isLoading().observe(this, Observer {
            if (it) {
                layoutLoading.visibility = View.VISIBLE
            } else {
                layoutLoading.visibility = View.GONE
            }
        })
    }

    private fun processWeather(weatherData: WeatherData) {
        tvLocation.text =
            getString(R.string.text_location, weatherData.cityName, weatherData.systemData.country)
        tvTime.text = getTime()
        tvTemp.text = getString(R.string.text_temp, getTemp(weatherData.mainData.temp))
    }

    private fun getTime(): String {
        val calendar: Calendar = Calendar.getInstance()
        val date: Date = calendar.time

        val dayOfWeek = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
        val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)

        return getString(R.string.text_time, dayOfWeek, time)
    }

    /**
     * Here we convert kelvin into celcius
     */
    private fun getTemp(temp: Double): Int {
        val toCelsius = temp - 273.15F;

        return toCelsius.roundToInt()
    }
}
