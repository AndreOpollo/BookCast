package com.opollo.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.opollo.auth.common.CustomButton
import com.opollo.auth.common.InputField

@Composable
fun LoginScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 32.dp, start=24.dp,end=24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ){
                Text(
                    text = "Welcome Back to BookCast",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Sign in to continue to your account",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black.copy(alpha = 0.4f),
                )
        Spacer(modifier = Modifier.height(24.dp))
        InputField(
            placeholder = "Email",
            value = "email@gmail.com",
            onValueChange = {},
        )
        Spacer(modifier = Modifier.height(16.dp))
        InputField(
            placeholder = "Password",
            value = "12345",
            onValueChange = {},
            isPassword = true
        )
        Spacer(modifier = Modifier.height(32.dp))
        CustomButton(
            title = "Login",
            onClick = {}
        )
        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Text("Don't have an account yet? ",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                letterSpacing = 0.8.sp)
            Text("Sign Up ",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                letterSpacing = 0.8.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(){
    LoginScreen()
}