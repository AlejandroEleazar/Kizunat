package com.example.kizunat.User

import java.util.Date

data class User(
    val name: String,
    val date_of_birth: Date,
    val gender: String,
    val height: Int,
    val weight: Int,
    val allerges: Set<String>,
    val activity_level: String
)
