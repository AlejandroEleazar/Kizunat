package com.example.kizunat.User

import com.google.firebase.Timestamp

data class User(
    val name: String,
    val date_of_birth: Timestamp,
    val gender: String,
    val height: Int,
    val weight: Int,
    val allergies: List<String>,
    val activity_level: String,
    val mail : String?
)
