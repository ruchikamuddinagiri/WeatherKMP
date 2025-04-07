package org.example.weather

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Greeting {
    private val platform = getPlatform()

    suspend fun greet(): String {
        // Call the suspend function and await the result
        val temperatureMessage = greetAndFetchTemperature("London")

        // Return the greeting with the temperature message
        return "Hello, ${platform.name} $temperatureMessage"
    }


    // Function that calls the suspend function to fetch temperature
    suspend fun greetAndFetchTemperature(city: String): String {
        return try {
            val temperatureMessage = getCityTemperature(city)  // Suspend function to get temperature
            println(temperatureMessage)  // Print the fetched temperature
            temperatureMessage
        } catch (e: Exception) {
            val errorMessage = "Error: ${e.message}"
            println(errorMessage)  // Error handling
            errorMessage
        }
    }
}