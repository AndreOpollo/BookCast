package com.opollo.player.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp



@Composable
fun SpeedSelectionDialog(
    currentSpeed:Float,
    onSpeedSelected:(Float)->Unit,
    onDismiss:()->Unit
){
    val speeds = listOf(0.5f,0.75f,1f,1.25f,1.5f,1.75f,2f)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Playback Speed",
                style = MaterialTheme.typography.headlineLarge)
        },
        text = {
            LazyColumn {
                items(speeds){
                    speed->
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clickable{onSpeedSelected(speed)}
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        RadioButton(
                            selected = speed == currentSpeed,
                            onClick = {onSpeedSelected(speed)}
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${speed}x",
                            style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

        },
        confirmButton = {
            TextButton(onDismiss) {
                Text("Done")
            }
        }
    )
}