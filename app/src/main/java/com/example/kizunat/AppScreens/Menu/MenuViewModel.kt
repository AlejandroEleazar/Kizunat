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

    private val _weeklyMeals = MutableStateFlow(List(7) { listOf<Recipe>() }) // 7 días, 3 comidas (breakfast, lunch, dinner)
    val weeklyMeals: StateFlow<List<List<Recipe>>> = _weeklyMeals

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadWeeklyMeals() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val allergies = AllergyRepository.getUserAllergies(uid)
                val dailyMeals = List(7) {
                    // Por cada día pedimos 3 comidas, puede mejorar con queries específicas si quieres
                    EdamamApi.fetchRecipes(listOf("breakfast", "lunch", "dinner"), allergies).take(3)
                }
                _weeklyMeals.value = dailyMeals
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
