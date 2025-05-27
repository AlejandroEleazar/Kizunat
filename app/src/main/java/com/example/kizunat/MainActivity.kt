package com.example.kizunat

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kizunat.NavigationWrapper.NavigationWrapper
import com.example.kizunat.ui.theme.KizunatTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth

class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        db = Firebase.firestore

        enableEdgeToEdge()

        setContent {
            KizunatTheme {
                NavigationWrapper(auth, db)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            Log.d(TAG, "Usuario autenticado: ${currentUser.email}")
            fetchMenuCollection()
        } else {
            Log.d(TAG, "Usuario NO autenticado")
            // Aquí podrías iniciar flujo de login si quieres
        }
    }

    private fun fetchMenuCollection() {
        db.collection("menu")
            .get()
            .addOnSuccessListener { documents ->
                Log.d(TAG, "Documentos encontrados en 'menu': ${documents.size()}")
                for (doc in documents) {
                    Log.d(TAG, "Documento ID: ${doc.id} => Data: ${doc.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error al obtener documentos: ", exception)
            }
    }
}
