package com.example.kizunat.AppScreens.Home

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kizunat.AppScreens.CustomScaffold
import com.example.kizunat.Model.Menu.Menu
import com.example.kizunat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    db: FirebaseFirestore,
    navigateToHome: () -> Unit,
    navigateToMenu: () -> Unit,
    navigateToProfile: () -> Unit
) {
    CustomScaffold(
        navigateToHome,
        navigateToMenu,
        navigateToProfile
    ) { padding ->
        Content(padding, db)
    }
}

@Composable
fun Content(padding: PaddingValues, db: FirebaseFirestore) {
    val currentDate = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }

    val meals = listOf(
        Meal("Breakfast", "Avocado Toast", R.drawable.bg, 350),
        Meal("Lunch", "Pasta With Pesto", R.drawable.bg, 760),
        Meal("Dinner", "Grilled Salmon", R.drawable.bg, 520)
    )

    val lazyListState = rememberLazyListState()
    val currentIndex by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex
        }
    }

    val currentMeal = meals.getOrNull(currentIndex) ?: meals.first()
    var menu by remember { mutableStateOf<Menu?>(null) }
    val uid = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(uid) {
        if (uid != null) {
            try {
                val snapshot = db.collection("menu").document(uid).get().await()
                menu = snapshot.toObject(Menu::class.java)
                Log.d("Firestore", "Usuario cargado: $menu")
            } catch (e: Exception) {
                Log.e("Firestore", "Error al obtener el usuario", e)
            }
        } else {
            Log.e("Auth", "Usuario no autenticado")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF9FAEF))
    ) {
        // Fondo con imagen
        Image(
            painter = painterResource(id = R.drawable.bg_3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Spacer(modifier = Modifier.height(70.dp))

            // LazyRow de comidas (desayuno, comida, cena)
            LazyRow(
                state = lazyListState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(meals) { meal ->
                    MealCard(currentDate, meal)
                }
            }
            Spacer(modifier = Modifier.height(33.dp))

            // Nutritional Summary
            Text(
                text = "Nutritional Summary",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.Start),
                color = Color(0xFF476730),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(20.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    NutrientCard("Food Calories", "${currentMeal.calories}KCal")
                }
                item {
                    NutrientCard("Calories Week", "2200KCal")
                }
            }

            Spacer(modifier = Modifier.height(23.dp))

            // Mood
            Text(
                text = "Mood",
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                      .align(Alignment.Start),
                color = Color(0xFF476730),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(23.dp))

            MoodSelector()

        }
    }
}

// Data class para comida
data class Meal(val type: String, val name: String, val imageRes: Int, val calories: Int)

@Composable
fun MealCard(date: String, meal: Meal) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .height(149.dp)
            .width(360.dp),
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        )
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
        ){
            Column(
                modifier = Modifier
                    .height(149.dp)
                    .width(160.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(Modifier.height(20.dp))

                Text(
                    date,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF476730),
                )

                Spacer(Modifier.height(20.dp))

                Text(
                    "${meal.name}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF476730),
                )
            }

            Column(
                modifier = Modifier
                    .height(149.dp)
                    .width(200.dp)
                    .clip(RoundedCornerShape(40.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = meal.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
                )
            }
        }


    }
}

@Composable
fun NutrientCard(title: String, value: String) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .size(175.dp, 100.dp),
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            
            Text(
                title,
                color = Color(0xFF476730),
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp,0.dp,0.dp,0.dp)
            )

            Spacer(Modifier.height(10.dp))

            Text(
                value,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF476730),
                fontSize = 20.sp
            )
        }
    }
}

@Composable
fun MoodSelector() {
    var selectedMoodIndex by remember { mutableStateOf(-1) }

    val moodIcons = listOf(
        Icons.Filled.SentimentVeryDissatisfied,
        Icons.Filled.SentimentDissatisfied,
        Icons.Filled.SentimentNeutral,
        Icons.Filled.SentimentSatisfied,
        Icons.Filled.SentimentVerySatisfied
    )
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .width(367.dp)
            .height(70.dp),
        shape = RoundedCornerShape(20.dp)
    ) {

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(12.dp)
                .fillMaxWidth()
                .height(70.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            moodIcons.forEachIndexed { index, icon ->
                val isSelected = index == selectedMoodIndex
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Color(0xFF4CAF50) else Color(0xFFE0E0E0))
                        .clickable { selectedMoodIndex = index },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) Color.White else Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
