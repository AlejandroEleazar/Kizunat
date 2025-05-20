package com.example.kizunat.AppScreens.Profile

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

@Composable
fun ProfileScreen(
    /* name: String,
     height: String,
     weight: String,
     mail: String,
     onEditClick: () -> Unit,*/
    navigateToHome: () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToMenu: () -> Unit,
    navigateToEditProfile: () -> Unit
){
    CustomScaffold(
        navigateToHome,
        navigateToMenu,
        navigateToProfile
    ) { padding ->
        ProfileContent(padding,navigateToEditProfile)
    }
}

@Composable
fun ProfileContent(padding: PaddingValues, navigateToEditProfile: () -> Unit) {
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
                .padding(top = 120.dp),  // Reducido el padding superior
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
                        InfoRow(Icons.Default.Person, /*name*/ "hola")
                        Divider(color = Color(0xFF476730), thickness = 1.dp)
                        InfoRow(Icons.Default.Straighten, /*height*/ "180")
                        Divider(color = Color(0xFF476730), thickness = 1.dp)
                        InfoRow(Icons.Default.FitnessCenter, /*weight*/ "79")
                        Divider(color = Color(0xFF476730), thickness = 1.dp)
                        InfoRow(Icons.Default.Email, /*mail*/ "ejemplo@gmail.com")
                        Divider(color = Color(0xFF476730), thickness = 1.dp)
                    }
                }


                // Círculo de la persona
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // más alto
                    shape = CircleShape,
                    modifier = Modifier
                        .size(180.dp)
                        .offset(y = (-80).dp)
                        .background(Color.Transparent) // sin fondo adicional
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

                // Círculo de la persona
                ElevatedCard(
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp), // más alto
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
                            modifier = Modifier.size(26.dp).clickable { navigateToEditProfile() }
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

