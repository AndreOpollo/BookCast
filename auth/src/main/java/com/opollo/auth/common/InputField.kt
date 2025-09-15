package com.opollo.auth.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InputField(
    value:String,
    placeholder:String,
    onValueChange:(String)->Unit,
    modifier: Modifier =Modifier,
    isPassword:Boolean = false
){
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = {onValueChange(it)},
        modifier = modifier.fillMaxWidth(),
        placeholder = {Text(placeholder)},
        trailingIcon = {
            if(isPassword){
                val icon = if(!passwordVisible) Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                val description = if(passwordVisible) "Hide Password" else "Show Password"

                IconButton(onClick = {passwordVisible = !passwordVisible}) {
                    Icon(icon, contentDescription = description)
                }
            }
        },
        visualTransformation = if(isPassword && !passwordVisible){
            PasswordVisualTransformation()
        }else{
            VisualTransformation.None
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Black.copy(alpha = 0.3f)
        )
    )
}


@Preview(showBackground = true)
@Composable
fun InputFieldPreview(){
    InputField(
        value = "email@gmail.com",
        placeholder = "Email",
        onValueChange = {},
    )
}

@Preview(showBackground = true)
@Composable
fun PasswordInputPreview(){
    InputField(
        value = "Hello",
        placeholder = "Password",
        onValueChange = {},
        isPassword = true
    )
}