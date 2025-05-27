package com.example.kizunat.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object AllergyRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getUserAllergies(userId: String): List<String> {
        return try {
            val snapshot = db.collection("users").document(userId).get().await()
            snapshot.get("allergies") as? List<String> ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
