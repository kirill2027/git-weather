package com.example.weather

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.getForObject

@Service
class WeatherService(
    @Value("\${weather-api.url}") private val weatherApiUrl: String,
    @Value("\${weather-api.api-key}") private val apiKey: String
) {
    fun getWeather(date: String): String {
        val location = "London" // Default location; change or make dynamic if needed.
        val url = "$weatherApiUrl?key=$apiKey&q=$location&dt=$date"

        val restTemplate = RestTemplate()
        val response: Map<String, Any> = restTemplate.getForObject(url)
            ?: throw RuntimeException("Failed to fetch weather data.")

        val weather = response["forecast"] as Map<*, *>? ?: throw RuntimeException("No forecast data available.")
        val forecast = weather["forecastday"] as List<*>
        val firstDay = forecast.first() as Map<*, *>
        val dayDetails = firstDay["day"] as Map<*, *>

        val condition = dayDetails["condition"] as Map<*, *>
        return "Temperature: ${dayDetails["avgtemp_c"]}°C, ${condition["text"]}"
    }
}