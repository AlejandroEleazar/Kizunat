package com.example.kizunat.AppScreens.Profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kizunat.AppScreens.CustomScaffold
import com.example.kizunat.R
import com.example.kizunat.User.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun ProfileScreen(
    db: FirebaseFirestore,
    navigateToHome: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToMenu: () -> Unit,
    navigateToEditProfile: () -> Unit,

){
    CustomScaffold(
        navigateToHome,
        navigateToMenu,
        navigateToProfile
    ) { padding ->
        ProfileContent(padding,navigateToEditProfile,db)
    }
}

@Composable
fun ProfileContent(
    padding: PaddingValues,
    navigateToEditProfile: () -> Unit,
    db: FirebaseFirestore
) {
    var user by remember { mutableStateOf<User?>(null) }
    val uid = FirebaseAuth.getInstance().currentUser?.uid

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
                .padding(top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(155.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                contentAlignment = Alignment.TopCenter
            ) {
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(490.dp),
                    shape = RoundedCornerShape(40.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFFFF)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 80.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Spacer(Modifier.height(10.dp))
                        InfoRow(Icons.Default.Person, user?.name ?: "Cargando...")
                        Divider(color = Color(0xFF476730), thickness = 1.dp)
                        InfoRow(Icons.Default.Straighten, user?.height?.toString() ?: "...")
                        Divider(color = Color(0xFF476730), thickness = 1.dp)
                        InfoRow(Icons.Default.FitnessCenter, user?.weight?.toString() ?: "...")
                        Divider(color = Color(0xFF476730), thickness = 1.dp)
                        InfoRow(Icons.Default.Email, user?.mail ?: "...")
                        Divider(color = Color(0xFF476730), thickness = 1.dp)
                    }
                }

                // Círculo de la persona
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

                // Botón editar
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(55.dp)
                        .offset(y = (-90).dp, x = 140.dp)
                        .background(Color.Transparent),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFFFF)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color(0xFF476730),
                            modifier = Modifier
                                .size(26.dp)
                                .clickable { navigateToEditProfile() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Icon",
            tint = Color(0xFF476730),
        )
        Spacer(Modifier.width(24.dp))
        Text(
            text = value,
            fontSize = 22.sp,  // Tamaño de texto aumentado
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF2C3E22)  // Color de texto más oscuro
        )
    }
}


suspend fun getUserInfo(db: FirebaseFirestore, uid: String): User? {
    val snapshot = db.collection("users").document(uid).get().await()
    return snapshot.toObject(User::class.java)
}

