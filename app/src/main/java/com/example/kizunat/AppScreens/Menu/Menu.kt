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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kizunat.AppScreens.CustomScaffold
import com.example.kizunat.R

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
        MenuContent(padding)
    }
}

@Composable
fun MenuContent(padding: PaddingValues) {
    var selectedDay by remember { mutableStateOf(0) }
    val days = listOf(
        "Monday", "Tuesday", "Wednesday",
        "Thursday", "Friday", "Saturday", "Sunday"
    )

    val mealsPerDay = listOf(
        Triple("Pancakes", "Chicken Salad", "Pasta"),
        Triple("Smoothie Bowl", "Burrito Bowl", "Sushi"),
        Triple("Toast & Eggs", "Veggie Wrap", "Steak"),
        Triple("Yogurt Parfait", "Rice & Beans", "Pizza"),
        Triple("Fruit Mix", "Grilled Chicken", "Curry"),
        Triple("Waffles", "Tofu Stir Fry", "Burger"),
        Triple("Bagel & Cream Cheese", "Poke Bowl", "Lasagna")
    )

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
                    val (breakfast, lunch, dinner) = mealsPerDay[selectedDay]
                    MealRow(Icons.Default.LocalCafe, "Breakfast", breakfast, Color(0xFFEDEFE4))
                    MealRow(Icons.Default.Fastfood, "Lunch", lunch, Color(0xFFEDEFE4))
                    MealRow(Icons.Default.Bedtime, "Dinner", dinner, Color(0xFFEDEFE4))

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { /* TODO: save to DB */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF476730)),
                        modifier = Modifier
                            .height(48.dp)
                            .width(200.dp),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = "Save",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MealRow(icon: ImageVector, label: String, foodName: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(36.dp))
            .background(color = backgroundColor, shape = RoundedCornerShape(36.dp))
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
                    text = foodName,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
