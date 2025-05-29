package com.example.kizunat.AppScreens.Menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kizunat.Model.Food.Food
import com.example.kizunat.Model.Menu.Menu
import com.example.kizunat.R
import com.example.kizunat.api.EdamamApi
import com.example.kizunat.api.Recipe
import com.example.kizunat.repository.AllergyRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
        List(7) { listOf(0, 0, 0) } // [breakfastIndex, lunchIndex, dinnerIndex] por día
    )
    val currentIndexes: StateFlow<List<List<Int>>> = _currentIndexes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val firestore = FirebaseFirestore.getInstance()

    fun loadWeeklyMeals() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val allergies = AllergyRepository.getUserAllergies(uid)
                Log.d("MenuViewModel", "✅ Alergias del usuario: $allergies")

                val breakfasts = try {
                    EdamamApi.fetchBreakfast(allergies).also {
                        Log.d("EdamamApi", "✅ Breakfast cargado (${it.size} recetas)")
                    }
                } catch (e: Exception) {
                    Log.e("EdamamApi", "❌ Error fetching breakfast: ${e.message}")
                    emptyList()
                }

                val lunches = try {
                    EdamamApi.fetchLunch(allergies).also {
                        Log.d("EdamamApi", "✅ Lunch cargado (${it.size} recetas)")
                    }
                } catch (e: Exception) {
                    Log.e("EdamamApi", "❌ Error fetching lunch: ${e.message}")
                    emptyList()
                }

                val dinners = try {
                    EdamamApi.fetchDinner(allergies).also {
                        Log.d("EdamamApi", "✅ Dinner cargado (${it.size} recetas)")
                    }
                } catch (e: Exception) {
                    Log.e("EdamamApi", "❌ Error fetching dinner: ${e.message}")
                    emptyList()
                }

                val weekMeals = List(7) {
                    DayMeals(
                        breakfasts = breakfasts.shuffled().take(3),
                        lunches = lunches.shuffled().take(3),
                        dinners = dinners.shuffled().take(3)
                    )
                }

                _weeklyMeals.value = weekMeals
                Log.d("MenuViewModel", "✅ Comidas cargadas (incluso si alguna categoría falló)")
            } catch (e: Exception) {
                Log.e("MenuViewModel", "❌ Error general al cargar comidas: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
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

        val newIndex = if (maxIndex == 0) 0 else (currentIndex + 1) % maxIndex

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

        val user = FirebaseAuth.getInstance().currentUser ?: return

        val data = Menu(
                Food(breakfast.label, breakfast.image, breakfast.calories.toInt()),
                Food(lunch.label, lunch.image, lunch.calories.toInt()),
                Food(dinner.label, dinner.image, dinner.calories.toInt())
        )



        firestore.collection("menus").document(user.uid)
            .set(data, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("MenuViewModel", "✅ Menú guardado correctamente")
            }
            .addOnFailureListener { e ->
                Log.e("MenuViewModel", "❌ Error al guardar el menú: ${e.message}")
            }
    }
}
