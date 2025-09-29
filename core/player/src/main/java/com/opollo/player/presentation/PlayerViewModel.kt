package com.opollo.player.presentation


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.runtime.currentComposer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
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
import androidx.core.net.toUri
import com.opollo.player.BackgroundPlayService

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val player: ExoPlayer
): ViewModel(){

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()

    private var progressJob: Job? = null

    init {
        player.addListener(object: Player.Listener{
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _uiState.update { it.copy(isPlaying = isPlaying) }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                if(mediaItem == null) return

                val newIndex = player.currentMediaItemIndex
                _uiState.update {
                    it.copy(
                        currentChapterIndex = newIndex,
                        currentChapter = it.chapters.getOrNull(newIndex),
                        playbackPosition = 0L,
                        currentDuration = player.duration
                    )
                }
            }
        })
        startProgressUpdates()
    }

    fun onEvent(e: PlayerEvent,context: Context? = null){
        when(e){
            is PlayerEvent.ChangePlaybackSpeed -> changeSpeed(e.speed)
            is PlayerEvent.LoadBook -> loadBook(e.book,e.chapters,context!!)
            is PlayerEvent.PlayChapter -> playChapter(e.index,context!!)
            PlayerEvent.PlayNext -> skipChapter(1,context!!)
            PlayerEvent.PlayPause -> togglePlayPause()
            PlayerEvent.PlayPrevious -> skipChapter(-1,context!!)
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

    private fun loadBook(book: Book,chapters: List<Chapter>,context: Context){
        _uiState.update {
            it.copy(
                currentBook = book,
                chapters = chapters,
                currentChapterIndex = 0,
                currentChapter = chapters.firstOrNull(),
                isLoading = true
            )
        }
        val authors = book.authors.joinToString(","){"${it.firstName} ${it.lastName}"}
        player.setMediaItems(
            chapters.map {
                chapter->
                MediaItem.Builder()
                    .setUri(chapter.audioUrl)
                    .setMediaId(chapter.chapterNumber.toString())
                    .setTag(chapter)
                    .setMediaMetadata(
                        MediaMetadata.Builder()
                            .setTitle("${book.title} - Chapter ${chapter.chapterNumber}")
                            .setArtist(authors)
                            .setArtworkUri(book.coverArt.toUri())
                            .build()
                    )
                    .build()
            }
        )

        player.prepare()
        _uiState.update {
            it.copy(isLoading = false)
        }
        playChapter(0,context)
    }

    private fun playChapter(index:Int,context: Context){
        if(index<0||index>_uiState.value.chapters.size) return
        player.seekTo(index,0L)
        player.playWhenReady = true

        startService(context)

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
    }

    private fun skipChapter(offset:Int,context: Context){
        val newIndex = _uiState.value.currentChapterIndex + offset
        playChapter(newIndex,context)
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

    private fun startService(context: Context){
        val intent = Intent(context,
            BackgroundPlayService::class.java)
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            context.startForegroundService(intent)
        }else{
            context.startService(intent)
        }
    }
    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}