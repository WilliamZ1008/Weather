package com.example.weather.model

data class WeatherData(
    val city: String,
    val date: String,
    val temperature: Double,
    val description: String,
    val updateTime: Long,
    val humidity: Int = 0,
    val windSpeed: Double = 0.0,
    val precipitation: Double = 0.0
) 