package com.example.kizunat.AppScreens.LogIn

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kizunat.R
import com.google.firebase.auth.FirebaseAuth


@Composable
fun AuthScreen(
    auth: FirebaseAuth,
    navigateToHome: () -> Unit,
    navigateToForm: () -> Unit,
    log: Boolean
) {
    var check by remember { mutableStateOf(log) }
    var mail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val cardModifier = if (check) {
        Modifier
            .width(367.dp)
            .height(498.dp)
            .padding(bottom = 18.dp)
    } else {
        Modifier
            .width(367.dp)
            .height(550.dp)
            .padding(bottom = 18.dp)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFF9FAEF))
    ) {
        // Fondo con imagen
        Image(
            painter = painterResource(id = R.drawable.bg_2),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = cardModifier,
                shape = RoundedCornerShape(40.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Btns(check = check, onToggle = { check = !check })

                    Text(
                        "Welcome to KIZUNAT",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF476730),
                        modifier = Modifier.padding(top = 24.dp)
                    )

                    Spacer(Modifier.height(20.dp))

                    if (check) {
                        FormInputRow("E-Mail", mail, { mail = it }, keyboardType = KeyboardType.Email)
                        PasswordInputRow("Password", password) { password = it }

                    } else {
                        
                        FormInputRow("Name", name, { name = it }, keyboardType = KeyboardType.Text)
                        FormInputRow("E-Mail", mail, { mail = it }, keyboardType = KeyboardType.Email)
                        PasswordInputRow("Password", password) { password = it }

                    }


                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (check) {
                                auth.signInWithEmailAndPassword(mail, password).addOnCompleteListener { task ->
                                    if (task.isSuccessful){
                                        navigateToHome()
                                    }
                                    else{
                                        Log.d("Error", "ERROR")
                                    }
                                }

                            }else{
                                auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener{ task ->
                                    if (task.isSuccessful){
                                        navigateToForm()
                                    }
                                    else{
                                        Log.d("Error", "ERROR")
                                    }
                                }

                            }
                        },
                        modifier = Modifier.size(308.dp, 50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF476730)
                        )
                    ) {
                        Text(
                            if (check) "Log In" else "Sign Up",
                            fontSize = 18.sp
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    Text(
                        "Forgot the password?",
                        fontSize = 18.sp,
                        color = Color(0xFF8C4A60),
                    )
                }
            }
        }
    }
}

@Composable
fun Btns(check: Boolean, onToggle: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 15.dp, top = 30.dp, end = 15.dp)
        ) {
            Button(
                onClick = onToggle,
                modifier = Modifier
                    .width(350.dp)
                    .height(50.dp)
                    .align(Alignment.TopCenter),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE0E0E0)
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (check) {
                        Spacer(modifier = Modifier.width(190.dp))
                        Text(
                            "Sign Up",
                            color = Color(0xFF8C4A60)
                        )
                    } else {
                        Spacer(modifier = Modifier.width(40.dp))
                        Text(
                            "Log In",
                            color = Color(0xFF8C4A60)
                        )
                    }
                }
            }
            Button(
                onClick = { /* acción adiós */ },
                modifier = Modifier
                    .width(180.dp)
                    .height(50.dp)
                    .align(if (check) Alignment.TopStart else Alignment.TopEnd),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF476730)
                )
            ) {
                Text(text = if (check) "Log In" else "Sign Up")
            }
        }
    }
}
@Composable
private fun FormInputRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(
        modifier = Modifier.padding(vertical = 5.dp, horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color(0xFF74796D),
                modifier = Modifier.weight(1f)
            )
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.width(223.dp),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFF476730)
                )
            )
        }
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

@Composable
private fun PasswordInputRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(vertical = 5.dp, horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color(0xFF74796D),
                modifier = Modifier.weight(1f)
            )

            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.width(223.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible) Icons.Default.Check else Icons.Default.Close
                    val description = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = icon, contentDescription = description, tint = Color(0xFF476730))
                    }
                },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color(0xFF476730)
                ),
                singleLine = true
            )
        }

        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}

