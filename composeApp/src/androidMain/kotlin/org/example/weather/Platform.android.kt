package org.example.weather

import android.os.Build
import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.json.JSONObject

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual suspend fun getCityTemperature(city: String): String {
    val apiKey = "15ed51d9bf3a0dbd8ca9da5942453e48"
    val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric"

    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }



    return try {
        // Running the API call on a background thread using withContext
        val response = withContext(Dispatchers.IO) {
            client.get(url)
        }
        val jsonResponse = JSONObject(response.bodyAsText())
        Log.d("API Response", response.bodyAsText())
        val temperature = jsonResponse.getJSONObject("main").getDouble("temp")
        "The current temperature in $city is $temperature°C"
    } catch (e: Exception) {
        "Failed to fetch data: ${e.message}"
    } finally {
        client.close()
    }
}