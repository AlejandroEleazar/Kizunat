package com.example.kizunat.NavigationWrapper

import kotlinx.serialization.Serializable

@Serializable
object Welcome

@Serializable
data class LoginSignup (
    val log : Boolean
)

@Serializable
data class Form (
    val name : String
)

@Serializable
object Home

@Serializable
object Menu

@Serializable
object Profile

@Serializable
object EditProfile
