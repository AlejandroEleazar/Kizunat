package com.example.kizunat.AppScreens.Welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kizunat.R


@Composable
fun welcome(navigateToLogIn: ( Boolean) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.bg_1),
            contentDescription = "bg",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )



        Column (
            modifier = Modifier.fillMaxSize().padding(0.dp,0.dp,0.dp,64.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Button(
                onClick = {navigateToLogIn(true)},
                modifier = Modifier
                    .padding(0.dp)
                    .width(308.dp)
                    .height(58.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF476730)
                )
            ) {
                Text(
                    text = "Log In",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFFFFFFFF)

                    )
                )
            }
            Spacer(Modifier.height(21.dp))
            Row (
                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.Center
            ){
                Text(
                    text = "Dont have an account?",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFFC8EEA8),
                    ),
                    modifier = Modifier
                        .width(197.dp)
                        .height(25.dp)
                )
                Text(
                    text = "Sign In",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF894A67),
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier
                        .width(60.dp)
                        .height(25.dp)
                        .clickable { navigateToLogIn(false)}
                )
            }
        }
    }
}

