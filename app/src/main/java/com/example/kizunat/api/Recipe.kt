package com.example.kizunat.api

import com.squareup.moshi.Json

data class RecipesResponse(
    val from: Int,
    val to: Int,
    val count: Int,
    val hits: List<Hit>
)

data class Hit(
    val recipe: Recipe
)

data class Recipe(
    @Json(name = "label")    val label: String,
    @Json(name = "calories") val calories: Double
)