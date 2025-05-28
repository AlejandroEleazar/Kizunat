package com.example.kizunat.api

import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeService {
    @GET("api/recipes/v2")
    suspend fun getRecipes(
        @Query("type")    type: String = "public",
        @Query("q")       query: String,
        @Query("app_id")  appId: String,
        @Query("app_key") appKey: String,
        @Query("excluded") excluded: String? = null,
        @Query("random") random: Boolean = true
    ): RecipesResponse

}