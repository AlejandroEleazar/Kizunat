package com.example.kizunat.AppScreens.Profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kizunat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// Define tu modelo User según los datos que usas:
data class User(
    val name: String? = null,
    val height: Double? = null,
    val weight: Double? = null,
    val mail: String? = null
)

@Composable
fun EditProfileScreen(
    db: FirebaseFirestore,
    navigateBack: () -> Unit
) {
    var user by remember { mutableStateOf<User?>(null) }
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    var name by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }

    LaunchedEffect(uid) {
        if (uid != null) {
            try {
                val snapshot = db.collection("users").document(uid).get().await()
                user = snapshot.toObject(User::class.java)
                Log.d("Firestore", "Usuario cargado: $user")
            } catch (e: Exception) {
                Log.e("Firestore", "Error al obtener el usuario", e)
            }
        } else {
            Log.e("Auth", "Usuario no autenticado")
        }
    }

    LaunchedEffect(user) {
        user?.let {
            name = it.name.orEmpty()
            height = it.height?.toInt()?.toString().orEmpty()
            weight = it.weight?.toInt()?.toString().orEmpty()
            mail = it.mail.orEmpty()
        }
    }

    var editingField by remember { mutableStateOf<String?>(null) }
    var editedValue by rememberSaveable { mutableStateOf("") }

    fun showEditDialog(field: String, currentValue: String) {
        editingField = field
        editedValue = currentValue
    }

    if (editingField != null) {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) { focusRequester.requestFocus() }

        AlertDialog(
            onDismissRequest = { /* No cerrar al tocar fuera */ },
            title = { Text("Editar $editingField") },
            text = {
                TextField(
                    value = editedValue,
                    onValueChange = { editedValue = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
            },
            confirmButton = {
                Button(onClick = {
                    when (editingField) {
                        "Nombre" -> name = editedValue
                        "Altura" -> height = editedValue
                        "Peso" -> weight = editedValue
                        "Correo" -> mail = editedValue
                    }
                    editingField = null
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { editingField = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_3),
            contentDescription = "Profile Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(155.dp))

            Box(
                modifier = Modifier.fillMaxWidth(0.9f),
                contentAlignment = Alignment.TopCenter
            ) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(520.dp),
                    shape = RoundedCornerShape(40.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 120.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        InfoRow2(value = name) { showEditDialog("Nombre", name) }
                        InfoRow2(value = height) { showEditDialog("Altura", height) }
                        InfoRow2(value = weight) { showEditDialog("Peso", weight) }
                        InfoRow2(value = mail) { showEditDialog("Correo", mail) }

                        Button(
                            onClick = {
                                uid?.let {
                                    val userRef = db.collection("users").document(it)

                                    val updates = mutableMapOf<String, Any>()
                                    if (name.isNotBlank()) updates["name"] = name
                                    height.toIntOrNull()?.let { h -> updates["height"] = h }
                                    weight.toIntOrNull()?.let { w -> updates["weight"] = w }
                                    if (mail.isNotBlank()) updates["mail"] = mail

                                    userRef.update(updates)
                                        .addOnSuccessListener {
                                            Log.d("Firestore", "Campos actualizados correctamente")
                                            navigateBack()
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("Firestore", "Error al actualizar campos", e)
                                        }
                                }
                            },
                            modifier = Modifier.size(308.dp, 50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF476730))
                        ) {
                            Text("Save", fontSize = 18.sp)
                        }
                    }
                }

                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(180.dp)
                        .offset(y = (-80).dp)
                        .background(Color.Transparent)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = Color(0xFF476730),
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow2(value: String?, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clickable(interactionSource = interactionSource, indication = null) {
                onClick()
            },
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F5EA))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = value ?: "No disponible",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C3E22)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Icon",
                tint = Color(0xFF476730)
            )
        }
    }
}
