package com.example.weather.network

import android.content.Context
import android.widget.Toast
import com.example.weather.model.WeatherData
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class WeatherApi(private val context: Context) {
    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .cache(Cache(context.cacheDir, 10 * 1024 * 1024)) // 10MB 缓存
        .build()

    fun fetchWeather(city: String, date: String, callback: (WeatherData?) -> Unit) {
        val weatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=YOUR_API_KEY&units=metric"
        
        val request = Request.Builder()
            .url(weatherUrl)
            .cacheControl(CacheControl.FORCE_NETWORK)
            .build()

        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                context.runOnUiThread {
                    Toast.makeText(context, "网络异常，请检查网络连接", Toast.LENGTH_SHORT).show()
                }
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    context.runOnUiThread {
                        Toast.makeText(context, "获取天气数据失败", Toast.LENGTH_SHORT).show()
                    }
                    callback(null)
                    return
                }

                try {
                    val body = response.body?.string()
                    val jsonObject = JSONObject(body)
                    
                    val main = jsonObject.getJSONObject("main")
                    val weather = jsonObject.getJSONArray("weather").getJSONObject(0)
                    val wind = jsonObject.getJSONObject("wind")

                    val weatherData = WeatherData(
                        city = city,
                        date = date,
                        temperature = main.getDouble("temp"),
                        description = weather.getString("description"),
                        updateTime = System.currentTimeMillis(),
                        humidity = main.getInt("humidity"),
                        windSpeed = wind.getDouble("speed"),
                        precipitation = jsonObject.optDouble("rain", 0.0)
                    )
                    
                    callback(weatherData)
                } catch (e: Exception) {
                    context.runOnUiThread {
                        Toast.makeText(context, "解析天气数据失败", Toast.LENGTH_SHORT).show()
                    }
                    callback(null)
                }
            }
        })
    }
} 