package com.example.kizunat.network

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

suspend fun fetchRecipe(query: String, allergies: List<String>): Recipe? {
    val appId = "efab9c70"
    val appKey = "fa64291ae5eba8538b85890add60ccae"

    return try {
        val response: EdamamResponse = KtorClient.httpClient.get("https://api.edamam.com/api/recipes/v2") {
            parameter("type", "public")
            parameter("q", query)
            parameter("app_id", appId)
            parameter("app_key", appKey)
            allergies.forEach {
                parameter("health", "${it.lowercase()}-free")
            }
        }.body()

        response.hits.firstOrNull()?.recipe
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Serializable
data class EdamamResponse(val hits: List<Hit>)

@Serializable
data class Hit(val recipe: Recipe)

@Serializable
data class Recipe(
    val label: String,
    val calories: Double,
    val image: String = ""
)
