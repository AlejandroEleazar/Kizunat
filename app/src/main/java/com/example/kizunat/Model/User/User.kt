package com.example.kizunat.User

import com.google.firebase.Timestamp
data class User(
    val name: String = "",
    val date_of_birth: Timestamp = Timestamp(0, 0),
    val gender: String = "",
    val height: Int = 0,
    val weight: Int = 0,
    val allergies: List<String> = emptyList(),
    val activity_level: String = "",
    val mail: String? = null
)
