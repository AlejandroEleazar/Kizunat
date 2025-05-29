package com.example.kizunat.AppScreens.Menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kizunat.AppScreens.CustomScaffold
import com.example.kizunat.AppScreens.Home.Content
import com.example.kizunat.R
import com.example.kizunat.api.Recipe
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun MenuScreen(
    navigateToHome: () -> Unit,
    navigateToMenu: () -> Unit,
    navigateToProfile: () -> Unit
) {
    CustomScaffold(
        navigateToHome,
        navigateToMenu,
        navigateToProfile
    ) { padding ->
        ContentMenu(padding)
    }
}

@Composable
fun ContentMenu(
    padding: PaddingValues,
    viewModel: MenuViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val meals by viewModel.weeklyMeals.collectAsState()
    val indexes by viewModel.currentIndexes.collectAsState()

    var selectedDay by remember { mutableStateOf(0) }
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    val backgroundImage = painterResource(id = R.drawable.bg_3)

    LaunchedEffect(Unit) {
        viewModel.loadWeeklyMeals()
    }
Box(
    modifier = Modifier.fillMaxSize()
) {
    Image(
        painter = backgroundImage,
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 220.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            modifier = Modifier.padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(days) { index, day ->
                Text(
                    text = day,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .clickable { selectedDay = index }
                        .padding(4.dp),
                    color = Color(0xFF476730),
                    fontWeight = if (index == selectedDay)
                        FontWeight.Bold
                    else
                        FontWeight.Normal
                )
            }
        }
            Spacer(Modifier.height(20.dp))
            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .width(380.dp)
                    .height(460.dp),
                shape = RoundedCornerShape(40.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
            ) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 24.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val dayMeals = meals.getOrNull(selectedDay)
                    val dayIndexes = indexes.getOrNull(selectedDay)

                    if (isLoading) {
                        CircularProgressIndicator()
                    } else if (dayMeals != null && dayIndexes != null) {
                        MealSelector(
                            icon = Icons.Default.LocalCafe,
                            title = "Breakfast",
                            options = dayMeals.breakfasts,
                            selectedIndex = dayIndexes[0]
                        ) { viewModel.setMeal(selectedDay, "breakfast", it) }

                        MealSelector(
                            icon = Icons.Default.Fastfood,
                            title = "Lunch",
                            options = dayMeals.lunches,
                            selectedIndex = dayIndexes[1]
                        ) { viewModel.setMeal(selectedDay, "lunch", it) }

                        MealSelector(
                            icon = Icons.Default.Bedtime,
                            title = "Dinner",
                            options = dayMeals.dinners,
                            selectedIndex = dayIndexes[2]
                        ) { viewModel.setMeal(selectedDay, "dinner", it) }

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = {
                                val breakfast = dayMeals.breakfasts.getOrNull(dayIndexes[0])
                                val lunch = dayMeals.lunches.getOrNull(dayIndexes[1])
                                val dinner = dayMeals.dinners.getOrNull(dayIndexes[2])
                                if (breakfast != null && lunch != null && dinner != null) {
                                    viewModel.saveUserMenu(breakfast, lunch, dinner)
                                }
                            },
                            shape = RoundedCornerShape(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF476730)),
                            modifier = Modifier
                                .width(340.dp)
                                .height(48.dp)
                        ) {
                            Text("Save", color = Color.White)
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun MealSelector(
    icon: ImageVector,
    title: String,
    options: List<Recipe>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(40.dp),
        shadowElevation = 6.dp,
        color = Color(0xFFEDEFE4)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF476730),
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF476730)
                    )
                    val recipe = options.getOrNull(selectedIndex)
                    if (recipe != null) {
                        Text(
                            text = recipe.label,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF476730)
                        )
                        val caloriesRounded = String.format("%.0f", recipe.calories)
                        Text(
                            text = "$caloriesRounded kcal",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF476730)
                        )
                    } else {
                        Text(
                            text = "No disponible",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF476730)
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Desplegar",
                    tint = Color(0xFF476730),
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { expanded = true }
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEachIndexed { index, recipe ->
                    DropdownMenuItem(
                        text = { Text(recipe.label) },
                        onClick = {
                            onSelected(index)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
