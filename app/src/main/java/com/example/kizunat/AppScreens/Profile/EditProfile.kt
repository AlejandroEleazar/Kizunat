package com.example.kizunat.AppScreens.Profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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


@Composable
fun EditProfileScreen(
    navigateBack: () -> Boolean
) {
    // Estados editables
    var name by remember { mutableStateOf(/*initialName*/ "hola") }
    var height by remember { mutableStateOf(/*initialHeight*/ "180") }
    var weight by remember { mutableStateOf(/*initialWeight*/ "79") }
    var mail by remember { mutableStateOf(/*initialMail*/ "ejemplo@gmail.com") }

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
                .padding(top = 120.dp),
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
                        .height(490.dp),
                    shape = RoundedCornerShape(40.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 80.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Spacer(Modifier.height(10.dp))

                        InfoRow2(value = name) { showEditDialog("Nombre", name) }
                        InfoRow2(value = height) { showEditDialog("Altura", height) }
                        InfoRow2(value = weight) { showEditDialog("Peso", weight) }
                        InfoRow2(value = mail) { showEditDialog("Correo", mail) }

                        Button(
                            onClick = {
                               navigateBack()
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
fun InfoRow2(value: String, onClick: () -> Unit) {
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
                text = value,
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


