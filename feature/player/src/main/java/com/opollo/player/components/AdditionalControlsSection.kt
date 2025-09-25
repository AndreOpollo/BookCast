package com.opollo.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AdditionalControlsSection(
    isRepeatEnabled:Boolean,
    playbackSpeed:Float,
    onRepeatToggle:()->Unit,
    onSpeedChange:(Float)->Unit
){
    var showSpeedDialog by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ){
        IconButton(onRepeatToggle) {
            Icon(
                imageVector = Icons.Default.Repeat,
                contentDescription = "Repeat",
                tint = if(isRepeatEnabled){
                    MaterialTheme.colorScheme.primary
                }else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }

            )
        }

        TextButton(onClick = {showSpeedDialog = true}) {
            Text(
                "${playbackSpeed}x",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    if(showSpeedDialog){
        SpeedSelectionDialog(
            currentSpeed = playbackSpeed,
            onSpeedSelected = {
                speed->
                onSpeedChange(speed)
                showSpeedDialog = false
            },
            onDismiss = { showSpeedDialog = false }
        )
    }
}