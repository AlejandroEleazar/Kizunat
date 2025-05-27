package com.example.kizunat.Model.User

data class User(
    val name: String = "",
    val dateOfBirth: String ="",
    val gender: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val allergies: List<String> = emptyList(),
    val activityLevel: String = "",
    val mail: String? = null
)
