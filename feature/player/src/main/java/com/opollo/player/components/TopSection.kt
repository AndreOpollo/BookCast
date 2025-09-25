package com.opollo.player.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.opollo.player.presentation.PlayerEvent
import com.opollo.player.presentation.PlayerUiState
import com.opollo.player.presentation.PlayerViewModel

@Composable
fun TopSection(
    showDropdownMenu:Boolean,
    onShowDropdownChange:(Boolean)->Unit,
    onShowQueue:()->Unit,
    viewModel: PlayerViewModel,
    state: PlayerUiState
){
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {  }
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Collapse",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Box{
            IconButton(
                onClick = { onShowDropdownChange(!showDropdownMenu) }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            DropdownMenu(
                expanded = showDropdownMenu,
                onDismissRequest = { onShowDropdownChange(false) }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "Queue",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    onClick = {
                        onShowQueue()
                        onShowDropdownChange(false)
                    },
                    leadingIcon = {
                        Icon(Icons.AutoMirrored.Filled.QueueMusic, contentDescription = null)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            "Sleep Timer",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    onClick = {

                        onShowDropdownChange(false)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Timer, contentDescription = null)
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            "Share",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    onClick = {
                        onShowDropdownChange(false)
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Share, contentDescription = null)
                    }
                )
            }
        }
    }
}