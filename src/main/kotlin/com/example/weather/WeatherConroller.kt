package com.example.weather

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class WeatherController(
    private val weatherService: WeatherService,
    private val chatGPTService: ChatGPTService
) {
    @GetMapping("/weather")
    fun getRecommendation(@RequestParam date: String): ResponseEntity<String> {
        return try {
            // Get weather data from WeatherService
            val weatherData = weatherService.getWeather(date)

            // Get clothing recommendation from ChatGPTService
            val recommendation = chatGPTService.getClothingRecommendation(date, weatherData)

            ResponseEntity.ok(recommendation)
        } catch (e: Exception) {
            ResponseEntity.badRequest().body("Error: ${e.message}")
        }
    }
}
