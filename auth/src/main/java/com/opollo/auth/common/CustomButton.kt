package com.opollo.auth.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    title:String,
    onClick:()->Unit,
    modifier: Modifier = Modifier
){
    Button(onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)) {
        Text(title, modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun CustomButtonPreview(){
    CustomButton(
        title = "Register",
        onClick = {}
    )
}