package com.example.weather.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.weather.model.WeatherData

class WeatherDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "weather.db"
        const val DATABASE_VERSION = 1
        const val TABLE_WEATHER = "weather"
        
        const val COLUMN_CITY = "city"
        const val COLUMN_DATE = "date"
        const val COLUMN_TEMP = "temp"
        const val COLUMN_DESC = "desc"
        const val COLUMN_UPDATE_TIME = "updateTime"
        const val COLUMN_HUMIDITY = "humidity"
        const val COLUMN_WIND_SPEED = "windSpeed"
        const val COLUMN_PRECIPITATION = "precipitation"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_WEATHER (
                $COLUMN_CITY TEXT,
                $COLUMN_DATE TEXT,
                $COLUMN_TEMP REAL,
                $COLUMN_DESC TEXT,
                $COLUMN_UPDATE_TIME INTEGER,
                $COLUMN_HUMIDITY INTEGER,
                $COLUMN_WIND_SPEED REAL,
                $COLUMN_PRECIPITATION REAL,
                PRIMARY KEY ($COLUMN_CITY, $COLUMN_DATE)
            )
        """.trimIndent()
        
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WEATHER")
        onCreate(db)
    }

    fun saveWeather(weather: WeatherData) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CITY, weather.city)
            put(COLUMN_DATE, weather.date)
            put(COLUMN_TEMP, weather.temperature)
            put(COLUMN_DESC, weather.description)
            put(COLUMN_UPDATE_TIME, weather.updateTime)
            put(COLUMN_HUMIDITY, weather.humidity)
            put(COLUMN_WIND_SPEED, weather.windSpeed)
            put(COLUMN_PRECIPITATION, weather.precipitation)
        }

        db.insertWithOnConflict(TABLE_WEATHER, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun getWeather(city: String, date: String): WeatherData? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_WEATHER,
            null,
            "$COLUMN_CITY = ? AND $COLUMN_DATE = ?",
            arrayOf(city, date),
            null,
            null,
            null
        )

        return if (cursor.moveToFirst()) {
            val weather = WeatherData(
                city = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY)),
                date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                temperature = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TEMP)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC)),
                updateTime = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_UPDATE_TIME)),
                humidity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_HUMIDITY)),
                windSpeed = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_WIND_SPEED)),
                precipitation = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRECIPITATION))
            )
            cursor.close()
            weather
        } else {
            cursor.close()
            null
        }
    }

    fun isWeatherDataExpired(updateTime: Long): Boolean {
        val currentTime = System.currentTimeMillis()
        val oneHourInMillis = 60 * 60 * 1000
        return (currentTime - updateTime) > oneHourInMillis
    }
} 