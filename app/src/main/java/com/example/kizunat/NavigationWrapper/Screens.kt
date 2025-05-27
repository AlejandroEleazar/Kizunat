package com.example.kizunat.NavigationWrapper

import kotlinx.serialization.Serializable

@Serializable
object Welcome

@Serializable
data class LoginSignup(val log: Boolean) {
    companion object
}

@Serializable
data class Form(val name: String) {
    companion object
}

@Serializable
object Home

@Serializable
object Menu

@Serializable
object Profile

@Serializable
object EditProfile
