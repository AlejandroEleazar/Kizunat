package com.example.kizunat.AppScreens.Menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kizunat.AppScreens.CustomScaffold
import com.example.kizunat.R
import com.example.kizunat.api.Recipe

@Composable
fun MenuScreen(
    navigateToHome: () -> Unit,
    navigateToMenu: () -> Unit,
    navigateToProfile: () -> Unit,
    viewModel: MenuViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val meals by viewModel.weeklyMeals.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadWeeklyMeals()
    }

    CustomScaffold(
        navigateToHome,
        navigateToMenu,
        navigateToProfile
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            MenuContent(padding, meals)
        }
    }
}

@Composable
fun MenuContent(padding: PaddingValues, meals: List<List<Recipe>>) {
    var selectedDay by remember { mutableStateOf(0) }

    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_3),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 240.dp, bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                itemsIndexed(days) { index, day ->
                    Text(
                        text = day,
                        fontSize = 22.sp,
                        fontWeight = if (index == selectedDay) FontWeight.Bold else FontWeight.Medium,
                        color = if (index == selectedDay) Color(0xFF38521E) else Color(0xFF6E6E6E),
                        modifier = Modifier
                            .clickable { selectedDay = index }
                            .padding(vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            ElevatedCard(
                shape = RoundedCornerShape(48.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .shadow(elevation = 12.dp, shape = RoundedCornerShape(48.dp))
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 24.dp, bottom = 16.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val dayMeals = meals.getOrNull(selectedDay) ?: emptyList()

                    if (dayMeals.size == 3) {
                        MealRow(Icons.Default.LocalCafe, "Breakfast", dayMeals[0])
                        MealRow(Icons.Default.Fastfood, "Lunch", dayMeals[1])
                        MealRow(Icons.Default.Bedtime, "Dinner", dayMeals[2])
                    } else {
                        Text("No meals available", modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun MealRow(icon: ImageVector, label: String, recipe: Recipe) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(36.dp))
            .background(color = Color(0xFFEDEFE4), shape = RoundedCornerShape(36.dp))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(40.dp),
                tint = Color(0xFF476730)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF476730)
                )
                Text(
                    text = recipe.label,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${recipe.calories.toInt()} kcal",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}
