package org.example.weather

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
expect suspend fun getCityTemperature(city: String): String