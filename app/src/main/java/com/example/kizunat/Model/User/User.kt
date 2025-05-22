package com.example.kizunat.User

import com.google.firebase.Timestamp
import java.util.Date

data class User(
    val name: String,
    val date_of_birth: Timestamp,
    val gender: String,
    val height: Int,
    val weight: Int,
    val allerges: List<String>,
    val activity_level: String
)
