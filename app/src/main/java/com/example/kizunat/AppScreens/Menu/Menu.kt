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
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kizunat.AppScreens.CustomScaffold
import com.example.kizunat.R
import com.example.kizunat.api.Recipe

@Composable
fun MenuScreen(
    navigateToHome: ()->Unit,
    navigateToMenu: ()->Unit,
    navigateToProfile: ()->Unit,
    viewModel: MenuViewModel = viewModel()
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val meals     by viewModel.weeklyMeals.collectAsState()

    LaunchedEffect(Unit) { viewModel.loadWeeklyMeals() }

    CustomScaffold(navigateToHome, navigateToMenu, navigateToProfile) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            MenuContent(meals)
        }
    }
}

@Composable
fun MenuContent(meals: List<List<Recipe>>) {
    var selectedDay by remember { mutableStateOf(0) }
    val days = listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")

    Column(Modifier.fillMaxSize()) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            itemsIndexed(days) { i, day ->
                Text(
                    text = day,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .clickable { selectedDay = i }
                        .padding(8.dp),
                    color = if (i==selectedDay) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onBackground
                )
            }
        }
        val todayMeals = meals.getOrNull(selectedDay) ?: emptyList()
        Spacer(Modifier.height(8.dp))
        MealRow(Icons.Default.LocalCafe, "Breakfast", todayMeals.getOrNull(0))
        MealRow(Icons.Default.Fastfood,   "Lunch",     todayMeals.getOrNull(1))
        MealRow(Icons.Default.Bedtime,    "Dinner",    todayMeals.getOrNull(2))
    }
}

@Composable
fun MealRow(icon: ImageVector, title: String, recipe: Recipe?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title, modifier = Modifier.size(32.dp))
        Spacer(Modifier.width(16.dp))
        Column(Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = recipe?.label ?: "No disponible", fontSize = 20.sp)
        }
        Text(
            text = recipe?.let { "${it.calories.toInt()} kcal" } ?: "-- kcal",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
