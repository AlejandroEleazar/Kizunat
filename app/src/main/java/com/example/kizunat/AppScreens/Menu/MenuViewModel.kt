package com.example.kizunat.AppScreens.Menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kizunat.api.EdamamApi
import com.example.kizunat.api.Recipe
import com.example.kizunat.repository.AllergyRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

data class DayMeals(
    val breakfasts: List<Recipe>,
    val lunches: List<Recipe>,
    val dinners: List<Recipe>
)

class MenuViewModel : ViewModel() {
    private val _weeklyMeals = MutableStateFlow(List(7) {
        DayMeals(emptyList(), emptyList(), emptyList())
    })
    val weeklyMeals: StateFlow<List<DayMeals>> = _weeklyMeals

    private val _currentIndexes = MutableStateFlow(
        List(7) { listOf(0, 0, 0) } // for each day: [breakfastIndex, lunchIndex, dinnerIndex]
    )
    val currentIndexes: StateFlow<List<List<Int>>> = _currentIndexes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val firestore = FirebaseFirestore.getInstance()

    fun loadWeeklyMeals() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        _isLoading.value = true
        viewModelScope.launch {
            val allergies = AllergyRepository.getUserAllergies(uid)

            val breakfasts = EdamamApi.fetchBreakfast(allergies)
            val lunches = EdamamApi.fetchLunch(allergies)
            val dinners = EdamamApi.fetchDinner(allergies)

            val weekMeals = List(7) { dayIndex ->
                DayMeals(
                    breakfasts = breakfasts.shuffled().take(3),
                    lunches = lunches.shuffled().take(3),
                    dinners = dinners.shuffled().take(3)
                )
            }

            _weeklyMeals.value = weekMeals
            _isLoading.value = false
        }
    }

    fun nextMeal(dayIndex: Int, mealType: String) {
        val current = _currentIndexes.value.toMutableList()
        val dayMeals = current[dayIndex].toMutableList()

        val maxIndex = when (mealType) {
            "breakfast" -> _weeklyMeals.value[dayIndex].breakfasts.size
            "lunch" -> _weeklyMeals.value[dayIndex].lunches.size
            else -> _weeklyMeals.value[dayIndex].dinners.size
        }

        val currentIndex = when (mealType) {
            "breakfast" -> dayMeals[0]
            "lunch" -> dayMeals[1]
            else -> dayMeals[2]
        }

        val newIndex = (currentIndex + 1) % maxIndex

        when (mealType) {
            "breakfast" -> dayMeals[0] = newIndex
            "lunch" -> dayMeals[1] = newIndex
            "dinner" -> dayMeals[2] = newIndex
        }

        current[dayIndex] = dayMeals
        _currentIndexes.value = current
    }

    fun setMeal(dayIndex: Int, mealType: String, newIndex: Int) {
        val current = _currentIndexes.value.toMutableList()
        val dayMeals = current[dayIndex].toMutableList()

        when (mealType) {
            "breakfast" -> dayMeals[0] = newIndex
            "lunch" -> dayMeals[1] = newIndex
            "dinner" -> dayMeals[2] = newIndex
        }

        current[dayIndex] = dayMeals
        _currentIndexes.value = current
    }

    fun saveUserMenu(breakfast: Recipe, lunch: Recipe, dinner: Recipe) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val menuData = hashMapOf(
            "breakfast" to mapOf(
                "label" to breakfast.label,
                "calories" to breakfast.calories
            ),
            "lunch" to mapOf(
                "label" to lunch.label,
                "calories" to lunch.calories
            ),
            "dinner" to mapOf(
                "label" to dinner.label,
                "calories" to dinner.calories
            ),
            "timestamp" to System.currentTimeMillis()
        )


        firestore.collection("users")
            .document(uid)
            .collection("menus")
            .add(menuData)
            .addOnSuccessListener {
                Log.d("MenuViewModel", "Menu guardado correctamente")
            }
            .addOnFailureListener { e ->
                Log.e("MenuViewModel", "Error guardando menú", e)
            }
    }
}
