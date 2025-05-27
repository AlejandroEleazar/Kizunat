package com.example.kizunat.api

import android.util.Log
import com.example.kizunat.network.KtorClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable

object EdamamApi {
    private const val APP_ID = "efab9c70"
    private const val APP_KEY = "fa64291ae5eba8538b85890add60ccae"

    suspend fun fetchRecipes(queryTerms: List<String>, allergies: List<String>): List<Recipe> {
        return try {
            val query = queryTerms.joinToString(",")
            val response: EdamamResponse = KtorClient.httpClient.get("https://api.edamam.com/api/recipes/v2") {
                parameter("type", "public")
                parameter("q", query)
                parameter("app_id", APP_ID)
                parameter("app_key", APP_KEY)
                allergies.forEach { allergy ->
                    parameter("excluded", allergy)
                }
            }.body()

            response.hits?.map { hit ->
                Recipe(
                    label = hit.recipe.label,
                    calories = hit.recipe.calories,
                    image = hit.recipe.image
                )
            } ?: emptyList()

        } catch (e: Exception) {
            Log.e("EdamamApi", "Error fetching recipes: ${e.message}", e)
            emptyList()
        }
    }

    @Serializable
    data class EdamamResponse(val hits: List<Hit>? = null)

    @Serializable
    data class Hit(val recipe: RecipeData)

    @Serializable
    data class RecipeData(
        val label: String,
        val calories: Double,
        val image: String
    )
}
