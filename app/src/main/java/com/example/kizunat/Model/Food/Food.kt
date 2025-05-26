package com.example.kizunat.Model.Food

import android.media.Image
import androidx.annotation.DrawableRes

data class Food(
    val name : String,
    @DrawableRes val img: Int,
    val cal : Int
)
