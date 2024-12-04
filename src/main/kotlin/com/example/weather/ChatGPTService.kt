package com.example.weather

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ChatGPTService(
    @Value("\${openai.api-key}") private val apiKey: String
) {
    fun getClothingRecommendation(date: String, weatherData: String): String {
        val prompt = """
            Date: $date
            Weather: $weatherData
            Based on the weather, what clothing is suitable for this day?
        """.trimIndent()

        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer $apiKey")
            contentType = MediaType.APPLICATION_JSON
        }

        val body = mapOf(
            "model" to "gpt-4",
            "messages" to listOf(mapOf("role" to "system", "content" to prompt))
        )

        val request = HttpEntity(body, headers)
        val restTemplate = RestTemplate()
        val response: Map<*, *> = restTemplate.postForObject(
            "https://api.openai.com/v1/chat/completions",
            request,
            Map::class.java
        ) ?: throw RuntimeException("Failed to fetch recommendation.")

        val choices = response["choices"] as List<*>
        val message = choices.first() as Map<*, *>
        val content = (message["message"] as Map<*, *>)["content"] as String
        return content
    }
}