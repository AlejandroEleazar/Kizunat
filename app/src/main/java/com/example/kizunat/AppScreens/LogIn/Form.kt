package com.example.kizunat.AppScreens.LogIn

import android.widget.DatePicker
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kizunat.R
import com.example.kizunat.viewmodels.FormViewModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@Composable
fun FormScreen(
    navigateToMenu: () -> Unit,
    db: FirebaseFirestore,
    name: String,
    formViewModel: FormViewModel = viewModel(
        factory = object : androidx.lifecycle.ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return FormViewModel(db) as T
            }
        }
    )
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var showGenderDialog by remember { mutableStateOf(false) }
    var showAllergiesDialog by remember { mutableStateOf(false) }
    var showActivityDialog by remember { mutableStateOf(false) }

    val datePicker = android.app.DatePickerDialog(
        context,
        { _: DatePicker, year, month, day ->
            formViewModel.dateOfBirth = "$day/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_4),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.75f)
                .align(Alignment.BottomCenter)
                .offset(y = (-20).dp),
            shape = RoundedCornerShape(40.dp),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Form",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF476730)
                )
                Spacer(Modifier.height(32.dp))

                // Date of Birth
                FormFieldRow("Date of Birth", formViewModel.dateOfBirth, onClick = { datePicker.show() }) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.Gray)
                }
                Spacer(Modifier.height(24.dp))

                // Gender
                FormOptionRow("Gender", formViewModel.selectedGender) { showGenderDialog = true }
                Spacer(Modifier.height(24.dp))

                // Height input
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Height",
                            fontSize = 17.sp,
                            color = Color.Black,
                            modifier = Modifier.weight(0.35f)
                        )
                        OutlinedTextField(
                            value = formViewModel.height,
                            onValueChange = { formViewModel.onHeightChange(it) },
                            isError = formViewModel.heightError,
                            singleLine = true,
                            modifier = Modifier
                                .weight(0.22f)
                                .height(48.dp),
                            trailingIcon = {
                                Text(
                                    "cm",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors()
                        )
                    }
                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                    if (formViewModel.heightError) {
                        Text(
                            "Please enter a valid height (0-999 cm)",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Weight input
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Weight",
                            fontSize = 17.sp,
                            color = Color.Black,
                            modifier = Modifier.weight(0.35f)
                        )
                        OutlinedTextField(
                            value = formViewModel.weight,
                            onValueChange = { formViewModel.onWeightChange(it) },
                            isError = formViewModel.weightError,
                            singleLine = true,
                            modifier = Modifier
                                .weight(0.22f)
                                .height(48.dp),
                            trailingIcon = {
                                Text(
                                    "kg",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    modifier = Modifier.padding(end = 12.dp)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors()
                        )
                    }
                    Divider(
                        color = Color.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                    if (formViewModel.weightError) {
                        Text(
                            "Please enter a valid weight (0-999 kg)",
                            color = Color.Red,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Allergies
                FormOptionRow("Allergies", formViewModel.selectedAllergies.joinToString()) {
                    showAllergiesDialog = true
                }
                Spacer(Modifier.height(24.dp))

                // Activity Level
                FormOptionRow("Activity Level", formViewModel.selectedActivity) {
                    showActivityDialog = true
                }

                Spacer(Modifier.height(40.dp))

                Button(
                    onClick = {
                        formViewModel.saveUser(name,
                            onSuccess = { navigateToMenu() },
                            onFailure = { /* manejar error aquí si quieres */ }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF476730)),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Next", color = Color.White, fontSize = 18.sp)
                }
            }
        }

        if (showGenderDialog) {
            SimpleDialog(
                title = "Select Gender",
                options = listOf("Male", "Female", "Other", "Prefer not to say"),
                selected = formViewModel.selectedGender,
                onDismiss = { showGenderDialog = false },
                onSelect = {
                    formViewModel.selectedGender = it
                    showGenderDialog = false
                }
            )
        }

        if (showAllergiesDialog) {
            MultiSelectDialog(
                title = "Select Allergies",
                options = listOf("Gluten", "Lactose", "Seafood", "Dried Fruits", "Eggs", "Soy"),
                selected = formViewModel.selectedAllergies,
                onDismiss = { showAllergiesDialog = false },
                onSelect = {
                    formViewModel.selectedAllergies = it
                    showAllergiesDialog = false
                }
            )
        }

        if (showActivityDialog) {
            SimpleDialog(
                title = "Select Activity Level",
                options = listOf("Low", "Medium", "High"),
                selected = formViewModel.selectedActivity,
                onDismiss = { showActivityDialog = false },
                onSelect = {
                    formViewModel.selectedActivity = it
                    showActivityDialog = false
                }
            )
        }
    }
}

@Composable
private fun FormFieldRow(
    label: String,
    value: String,
    onClick: () -> Unit,
    trailingIcon: @Composable () -> Unit
) {
    Column(Modifier.padding(vertical = 8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontSize = 17.sp, color = Color.Black, modifier = Modifier.weight(1f))
            Text(value, fontSize = 17.sp, color = Color.DarkGray, textAlign = TextAlign.End)
            Spacer(Modifier.width(12.dp))
            trailingIcon()
        }
        Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(top = 6.dp))
    }
}

@Composable
private fun FormOptionRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    FormFieldRow(label, value, onClick) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun SimpleDialog(
    title: String,
    options: List<String>,
    selected: String,
    onDismiss: () -> Unit,
    onSelect: (String) -> Unit
) {
    var temp by remember { mutableStateOf(selected) }
    Dialog(onDismissRequest = onDismiss) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(Modifier.padding(24.dp)) {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { temp = option }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(option, fontSize = 15.sp)
                        Switch(
                            checked = temp == option,
                            onCheckedChange = null,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF476730),
                                checkedTrackColor = Color(0x80476730)
                            )
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        onSelect(temp)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF476730),
                        contentColor = Color.White
                    )
                ) {
                    Text("Finish")
                }
            }
        }
    }
}

@Composable
private fun MultiSelectDialog(
    title: String,
    options: List<String>,
    selected: List<String>,
    onDismiss: () -> Unit,
    onSelect: (List<String>) -> Unit
) {
    var temp by remember { mutableStateOf(selected.toSet()) }

    Dialog(onDismissRequest = onDismiss) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(Modifier.padding(24.dp)) {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                options.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                temp = temp.toMutableSet().apply {
                                    if (contains(option)) remove(option) else add(option)
                                }
                            }
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(option, fontSize = 17.sp)
                        Switch(
                            checked = temp.contains(option),
                            onCheckedChange = null,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFF476730),
                                checkedTrackColor = Color(0x80476730)
                            )
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        onSelect(temp.toList())
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF476730),
                        contentColor = Color.White
                    )
                ) {
                    Text("Finish")
                }
            }
        }
    }
}
