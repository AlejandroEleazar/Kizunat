package com.example.kizunat.AppScreens.Home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kizunat.Model.Menu.Menu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeViewModel(private val db: FirebaseFirestore) : ViewModel() {

    private val _menu = MutableStateFlow<Menu?>(null)
    val menu: StateFlow<Menu?> get() = _menu

    init {
        fetchMenu()
    }

    private fun fetchMenu() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid == null) {
            Log.e("Auth", "Usuario no autenticado")
            return
        }

        viewModelScope.launch {
            try {
                val snapshot = db.collection("menus").document(uid).get().await()
                _menu.value = snapshot.toObject(Menu::class.java)
                Log.d("Firestore", "Usuario cargado: ${_menu.value}")
            } catch (e: Exception) {
                Log.e("Firestore", "Error al obtener el usuario", e)
            }
        }
    }
}
