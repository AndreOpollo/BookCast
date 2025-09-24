package com.opollo.player.presentation

import android.util.Log
import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.opollo.domain.model.Book
import com.opollo.domain.model.Chapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val player: ExoPlayer
): ViewModel(){

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()

    private var progressJob: Job? = null

    init {
        startProgressUpdates()
    }

    fun onEvent(e: PlayerEvent){
        when(e){
            is PlayerEvent.ChangePlaybackSpeed -> changeSpeed(e.speed)
            is PlayerEvent.LoadBook -> loadBook(e.book,e.chapters)
            is PlayerEvent.PlayChapter -> playChapter(e.index)
            PlayerEvent.PlayNext -> skipChapter(1)
            PlayerEvent.PlayPause -> togglePlayPause()
            PlayerEvent.PlayPrevious -> skipChapter(-1)
            is PlayerEvent.SeekTo -> seekTo(e.position)
            PlayerEvent.Stop -> stop()
        }
    }

    private fun startProgressUpdates(){
        progressJob?.cancel()
        progressJob = viewModelScope.launch {
            while (isActive){

                val position = player.currentPosition
                val duration = player.duration

                _uiState.update {
                    it.copy(
                        playbackPosition = position,
                        currentDuration = if(duration>0) duration else it.currentDuration
                    )
                }
                delay(100L)
            }
        }
    }

    private fun loadBook(book: Book,chapters: List<Chapter>){
        _uiState.update {
            it.copy(
                currentBook = book,
                chapters = chapters,
                currentChapterIndex = 0,
                currentChapter = chapters.firstOrNull(),
                isLoading = true
            )
        }
        player.setMediaItems(
            chapters.map {
                chapter->
                MediaItem.Builder()
                    .setUri(chapter.audioUrl)
                    .setMediaId(chapter.chapterNumber.toString())
                    .setTag(chapter)
                    .build()
            }
        )

        player.prepare()
        _uiState.update {
            it.copy(isLoading = false)
        }
    }

    private fun playChapter(index:Int){
        if(index<0||index>_uiState.value.chapters.size) return
        player.seekTo(index,0L)
        player.playWhenReady = true

        val chapter = _uiState.value.chapters[index]
        _uiState.update {
            it.copy(
                isPlaying = true,
                currentChapter = chapter,
                currentChapterIndex = index,
                playbackPosition = 0L,
                currentDuration = 0L
            )
        }
    }

    private fun togglePlayPause(){
        val isPlaying = player.isPlaying
        if(isPlaying) player.pause() else player.play()
        _uiState.update {
            it.copy(isPlaying = !isPlaying)
        }
    }

    private fun skipChapter(offset:Int){
        val newIndex = _uiState.value.currentChapterIndex + offset
        playChapter(newIndex)
    }

    private fun seekTo(position:Long){
        player.seekTo(position)
        _uiState.update {
            it.copy(playbackPosition = position)
        }
    }

    private fun changeSpeed(speed:Float){
        player.setPlaybackSpeed(speed)
        _uiState.update {
            it.copy(playbackSpeed = speed)
        }
    }

    private fun stop(){
        player.stop()
        _uiState.update {
            it.copy(isPlaying = false)
        }
    }
    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}