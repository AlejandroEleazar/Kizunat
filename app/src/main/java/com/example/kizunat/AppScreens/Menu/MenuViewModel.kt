package com.example.kizunat.AppScreens.Menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kizunat.api.EdamamApi
import com.example.kizunat.api.Recipe
import com.example.kizunat.repository.AllergyRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MenuViewModel : ViewModel() {
    private val _weeklyMeals = MutableStateFlow(List(5) { listOf<Recipe>() })
    val weeklyMeals: StateFlow<List<List<Recipe>>> = _weeklyMeals

    private val _isLoading   = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadWeeklyMeals() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        _isLoading.value = true
        viewModelScope.launch {
            val allergies = AllergyRepository.getUserAllergies(uid)
            val data = List(5) { dayIndex ->
                // fetch and rotate to get different meals per day
                val b = EdamamApi.fetchBreakfast(allergies).getOrNull(dayIndex % 5)
                val l = EdamamApi.fetchLunch(allergies).getOrNull((dayIndex+1) % 5)
                val d = EdamamApi.fetchDinner(allergies).getOrNull((dayIndex+2) % 5)
                listOfNotNull(b,l,d)
            }
            _weeklyMeals.value = data
            _isLoading.value = false
        }
    }
    // MenuViewModel.kt (añade dentro de la clase)
    fun changeMeal(dayIndex: Int, mealType: String) {
        // Aquí vuelves a pedir un nuevo plato para ese día y tipo
        viewModelScope.launch {
            val allergies = AllergyRepository.getUserAllergies(FirebaseAuth.getInstance().currentUser?.uid ?: return@launch)
            val newRecipe = when (mealType) {
                "breakfast" -> EdamamApi.fetchBreakfast(allergies).randomOrNull()
                "lunch"     -> EdamamApi.fetchLunch(allergies).randomOrNull()
                else        -> EdamamApi.fetchDinner(allergies).randomOrNull()
            }
            if (newRecipe != null) {
                val updatedDay = _weeklyMeals.value.toMutableList()
                val mealsForDay = updatedDay[dayIndex].toMutableList()
                when (mealType) {
                    "breakfast" -> if (mealsForDay.size > 0) mealsForDay[0] = newRecipe
                    "lunch"     -> if (mealsForDay.size > 1) mealsForDay[1] = newRecipe
                    "dinner"    -> if (mealsForDay.size > 2) mealsForDay[2] = newRecipe
                }
                updatedDay[dayIndex] = mealsForDay
                _weeklyMeals.value = updatedDay
            }
        }
    }

}
