package com.example.kizunat.Model.Menu

import com.example.kizunat.Model.Food.Food

data class Menu(
    val breakfast: Food? = null,
    val lunch: Food? = null,
    val dinner: Food? = null
)
