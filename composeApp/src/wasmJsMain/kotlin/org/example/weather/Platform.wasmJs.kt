package org.example.weather

import io.ktor.client.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

actual suspend fun getCityTemperature(city: String): String {

    val apiKey = "15ed51d9bf3a0dbd8ca9da5942453e48"
    val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$apiKey&units=metric"

    val client = HttpClient(Js) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
    return try {
        // Perform the HTTP request to fetch weather data
        val response = withContext(Dispatchers.Default) {
            client.get(url)
        }

        // Parse JSON response to extract the temperature
        val jsonResponse = Json.parseToJsonElement(response.bodyAsText()).jsonObject
        val temperature = jsonResponse["main"]?.jsonObject?.get("temp")?.jsonPrimitive?.double ?: 0.0
        "The current temperature in $city is $temperature°C"
    } catch (e: Exception) {
        "Failed to fetch data: ${e.message}"
    } finally {
        client.close()
    }
}