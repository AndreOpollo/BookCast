package com.opollo.player

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PauseCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.opollo.player.presentation.PlayerEvent
import com.opollo.player.presentation.PlayerViewModel

@Composable
fun FullScreenPlayer(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = hiltViewModel()
){
    val state by viewModel.uiState.collectAsState()
    var tempSeek by remember { mutableStateOf<Long?>(null) }
    val currentDuration = state.currentDuration
    val currentPosition = tempSeek?:state.playbackPosition

    LaunchedEffect(state.playbackPosition){
        Log.d("Playback Position","${state.playbackPosition}")
        Log.d("Playback Position-CurrentPosition","$currentPosition")

    }
    Column(
        modifier = modifier.fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ){
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(state.currentBook?.coverArt?:"")
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = "",
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = state.currentChapter?.title?:"",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = state.currentBook?.title?:"",
                style = MaterialTheme.typography.titleMedium,
            )
        }
        Column {
            Slider(
                value = if(currentDuration>0) currentPosition.toFloat() else 0f,
                onValueChange = {
                    tempSeek = it.toLong()
                },
                onValueChangeFinished = {
                    tempSeek?.let {
                        seek->
                        viewModel.onEvent(PlayerEvent.SeekTo(seek))
                    }
                    tempSeek = null
                },
                valueRange = 0f..currentDuration.toFloat()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(formatTime(state.playbackPosition))
                Text(formatTime(currentDuration))
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(onClick = {
                viewModel.onEvent(PlayerEvent.PlayPrevious)
            }) {
                Icon(Icons.Default.SkipPrevious,
                    "Previous",
                    modifier = Modifier.size(40.dp))
            }
            IconButton(onClick = {
                viewModel.onEvent(PlayerEvent.PlayPause)
            }) {
                Icon(if(state.isPlaying)Icons.Filled.PauseCircle
                else Icons.Filled.PlayCircle,
                    "Play/Pause",
                    modifier = Modifier.size(72.dp))
            }
            IconButton(onClick = { viewModel.onEvent(PlayerEvent.PlayNext)}) {
                Icon(Icons.Default.SkipNext,
                    "Next",
                    modifier = Modifier.size(40.dp))
            }

        }

    }
}

private fun formatTime(timeMs:Long):String{
    val totalSeconds = timeMs/1000
    val minutes = totalSeconds/60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}