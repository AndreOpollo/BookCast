package com.opollo.player

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.opollo.player.components.BookArtSection
import com.opollo.player.components.BookInfoSection
import com.opollo.player.components.PlayerControlsSection
import com.opollo.player.components.ProgressSection
import com.opollo.player.components.QueueBottomSheet
import com.opollo.player.components.TopSection
import com.opollo.player.presentation.PlayerEvent
import com.opollo.player.presentation.PlayerViewModel

@Composable
fun FullScreenPlayer(
    modifier: Modifier = Modifier,
    viewModel: PlayerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var tempSeek by remember { mutableStateOf<Long?>(null) }
    var showDropdownMenu by remember { mutableStateOf(false) }
    var showQueueSheet by remember { mutableStateOf(false) }

    val currentDuration = state.currentDuration
    val currentPosition = tempSeek ?: state.playbackPosition


    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopSection(
            showDropdownMenu = showDropdownMenu,
            onShowDropdownChange = { showDropdownMenu = it },
            onShowQueue = { showQueueSheet = true },
            viewModel = viewModel,
            state = state
        )

        Spacer(modifier = Modifier.height(16.dp))

        BookArtSection(
            coverArt = state.currentBook?.coverArt,
            modifier = Modifier.weight(1f, false)
        )

        Spacer(modifier = Modifier.height(24.dp))

        BookInfoSection(
            chapterTitle = state.currentChapter?.title,
            bookTitle = state.currentBook?.title
        )

        Spacer(modifier = Modifier.height(32.dp))
        ProgressSection(
            currentPosition = currentPosition,
            currentDuration = currentDuration,
            tempSeek = tempSeek,
            onSeekChange = { tempSeek = it.toLong() },
            onSeekFinished = {
                tempSeek?.let { seek ->
                    viewModel.onEvent(PlayerEvent.SeekTo(seek))
                }
                tempSeek = null
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        PlayerControlsSection(
            isPlaying = state.isPlaying,
            onPrevious = { viewModel.onEvent(PlayerEvent.PlayPrevious,context) },
            onPlayPause = { viewModel.onEvent(PlayerEvent.PlayPause,context) },
            onNext = { viewModel.onEvent(PlayerEvent.PlayNext,context) },
            onRewind = {  },
            onFastForward = {  }
        )

        Spacer(modifier = Modifier.height(16.dp))

    }

    if (showQueueSheet) {
        QueueBottomSheet(
            onDismiss = { showQueueSheet = false },
            viewModel = viewModel,
            state = state
        )
    }
}

