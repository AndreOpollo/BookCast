package com.opollo.player


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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.opollo.player.presentation.PlayerEvent
import com.opollo.player.presentation.PlayerViewModel


@Composable
fun MiniPlayer(
    onExpand:()->Unit,
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = hiltViewModel()
){
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    Box(
        modifier = modifier.fillMaxWidth()
            .height(72.dp)
            .clickable(onClick = onExpand),
    ){
        Row(
            modifier = Modifier.fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier.size(48.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(state.currentBook?.coverArt ?: "")
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(com.opollo.player.R.drawable.placeholder),
                        contentDescription = "Book Cover",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        state.currentChapter?.title ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        state.currentBook?.title ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Box(modifier = Modifier.size(48.dp),
                contentAlignment = Alignment.Center){
                if(state.isLoading || state.isBuffering){
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    IconButton(onClick = { viewModel.onEvent(PlayerEvent.PlayPause, context) }) {
                        val icon =
                            if (state.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow
                        Icon(
                            icon,
                            contentDescription = "Play/Pause",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

            }
        }
        LinearProgressIndicator(
            progress = {
                if(state.currentDuration>0){
                    state.playbackPosition.toFloat()/state.currentDuration
                }else{
                    0f
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.BottomCenter)
        )
    }
}