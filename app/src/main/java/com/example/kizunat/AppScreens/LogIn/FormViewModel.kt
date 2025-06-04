package com.example.kizunat.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.kizunat.Model.User

class FormViewModel(private val db: FirebaseFirestore) : ViewModel() {

    var dateOfBirth by mutableStateOf("")
    var height by mutableStateOf("")
    var weight by mutableStateOf("")
    var selectedGender by mutableStateOf("")
    var selectedAllergies by mutableStateOf(listOf<String>())
    var selectedActivity by mutableStateOf("")

    var heightError by mutableStateOf(false)
    var weightError by mutableStateOf(false)

    fun onHeightChange(input: String) {
        if (input.length <= 3 && input.all { it.isDigit() }) {
            height = input
            heightError = input.toIntOrNull()?.let { it !in 0..999 } ?: true
        }
    }

    fun onWeightChange(input: String) {
        if (input.length <= 3 && input.all { it.isDigit() }) {
            weight = input
            weightError = input.toIntOrNull()?.let { it !in 0..999 } ?: true
        }
    }

    fun saveUser(name: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser
        val uid = user?.uid

        uid?.let {
            val userData = User(
                name = name,
                dateOfBirth = dateOfBirth,
                gender = selectedGender,
                height = height.toIntOrNull() ?: 0,
                weight = weight.toIntOrNull() ?: 0,
                allergies = selectedAllergies,
                activityLevel = selectedActivity,
                mail = user?.email
            )

            db.collection("users").document(it).set(userData)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }
}
