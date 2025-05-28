package com.example.kizunat.api

import android.util.Log
import com.example.kizunat.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object EdamamApi {
    private const val APP_ID  = "9987b754"
    private const val APP_KEY = "65c9299ec4f9dc2f3e613447f6f20f26"
    private val service = RetrofitClient.retrofit.create(RecipeService::class.java)

    private fun excludedParam(allergies: List<String>) =
        allergies.takeIf { it.isNotEmpty() }?.joinToString(",")

    suspend fun fetchBreakfast(allergies: List<String>) = fetchByMeal("breakfast", allergies)
    suspend fun fetchLunch(allergies: List<String>)     = fetchByMeal("lunch",     allergies)
    suspend fun fetchDinner(allergies: List<String>)    = fetchByMeal("dinner",    allergies)

    private suspend fun fetchByMeal(meal: String, allergies: List<String>) = try {
        withContext(Dispatchers.IO) {
            val resp = service.getRecipes(
                query    = meal,
                appId    = APP_ID,
                appKey   = APP_KEY,
                excluded = excludedParam(allergies)
            )
            resp.hits.map { it.recipe }
        }
    } catch (e: Exception) {
        Log.e("EdamamApi", "Error fetching $meal: ${e.message}", e)
        emptyList()
    }
}
