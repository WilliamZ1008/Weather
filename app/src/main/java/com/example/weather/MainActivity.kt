package com.example.weather

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.db.WeatherDbHelper
import com.example.weather.model.WeatherData
import com.example.weather.network.WeatherApi
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var weatherApi: WeatherApi
    private lateinit var dbHelper: WeatherDbHelper
    private var selectedDate = "今天"
    private var isFirstLoad = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        weatherApi = WeatherApi(this)
        dbHelper = WeatherDbHelper(this)

        setupSpinner()
        setupSwipeRefresh()
        setupSearchButton()
        
        // 显示默认的假天气信息
        showDefaultWeather()
    }

    private fun showDefaultWeather() {
        val defaultWeather = WeatherData(
            city = "西安",
            date = selectedDate,
            temperature = 25.0,
            description = "晴朗",
            updateTime = System.currentTimeMillis(),
            humidity = 45,
            windSpeed = 3.5,
            precipitation = 0.0
        )
        updateUI(defaultWeather)
        binding.editCity.setText("Beijing")
    }

    private fun setupSpinner() {
        val dates = arrayOf("今天", "明天", "后天")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, dates)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDate.adapter = adapter

        binding.spinnerDate.onItemSelectedListener = object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                selectedDate = dates[position]
                val city = binding.editCity.text.toString()
                if (city.isNotEmpty() && !isFirstLoad) {
                    fetchWeather(city, selectedDate)
                }
            }

            override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {}
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            val city = binding.editCity.text.toString()
            if (city.isNotEmpty()) {
                fetchWeather(city, selectedDate)
            } else {
                binding.swipeRefresh.isRefreshing = false
            }
        }
    }

    private fun setupSearchButton() {
        binding.buttonSearch.setOnClickListener {
            val city = binding.editCity.text.toString()
            if (city.isEmpty()) {
                Toast.makeText(this, "请输入城市名称", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            isFirstLoad = false
            fetchWeather(city, selectedDate)
        }
    }

    private fun fetchWeather(city: String, date: String) {
        binding.swipeRefresh.isRefreshing = true
        
        // 先检查缓存
        val cachedWeather = dbHelper.getWeather(city, date)
        if (cachedWeather != null && !dbHelper.isWeatherDataExpired(cachedWeather.updateTime)) {
            updateUI(cachedWeather)
            binding.swipeRefresh.isRefreshing = false
            return
        }

        // 如果缓存不存在或已过期，则从网络获取
        weatherApi.fetchWeather(city, date) { weather ->
            binding.swipeRefresh.isRefreshing = false
            if (weather != null) {
                dbHelper.saveWeather(weather)
                updateUI(weather)
            }
        }
    }

    private fun updateUI(weather: WeatherData) {
        binding.textTemperature.text = "${weather.temperature}°C"
        binding.textDescription.text = weather.description
        binding.textHumidity.text = "湿度: ${weather.humidity}%"
        binding.textWindSpeed.text = "风速: ${weather.windSpeed} m/s"
        
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        binding.textUpdateTime.text = "更新时间: ${dateFormat.format(Date(weather.updateTime))}"

        // 根据天气描述设置图标
        val iconResId = when {
            weather.description.contains("雨") -> R.drawable.ic_rain
            weather.description.contains("雪") -> R.drawable.ic_snow
            weather.description.contains("云") -> R.drawable.ic_cloud
            else -> R.drawable.ic_sunny
        }
        binding.imageWeather.setImageResource(iconResId)
    }
}