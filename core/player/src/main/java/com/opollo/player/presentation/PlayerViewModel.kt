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
import com.opollo.domain.model.ReadingProgress
import com.opollo.domain.repository.AuthRepository
import com.opollo.domain.repository.ReadingProgressRepository
import com.opollo.domain.util.Resource
import com.opollo.player.BackgroundPlayService
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.util.concurrent.TimeUnit

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val player: ExoPlayer,
    private val repository: ReadingProgressRepository,
    private val authRepository: AuthRepository
): ViewModel(){

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()

    private var progressJob: Job? = null

    private var lastSaveTimeMillis = 0L
    private val SAVE_INTERVAL_MILLIS = 15_000L

    init {
        player.addListener(object: Player.Listener{
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _uiState.update { it.copy(isPlaying = isPlaying) }
                if(!isPlaying){
                    saveCurrentProgress()
                }
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                _uiState.update {
                    it.copy(isBuffering = playbackState == Player.STATE_BUFFERING)
                }
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
                saveCurrentProgress()
            }
        })
        startProgressUpdates()
    }

    fun onEvent(e: PlayerEvent,context: Context? = null){
        when(e){
            is PlayerEvent.ChangePlaybackSpeed -> changeSpeed(e.speed)
            is PlayerEvent.LoadBook ->{
                Log.d("LoadBook","Triggered")
                loadBook(e.book,e.chapters,context!!)}
            is PlayerEvent.PlayChapter -> playChapter(e.index,context!!)
            PlayerEvent.PlayNext -> skipChapter(1,context!!)
            PlayerEvent.PlayPause -> togglePlayPause(context!!)
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
                val currentTime = System.currentTimeMillis()
                if (player.isPlaying && (currentTime - lastSaveTimeMillis > SAVE_INTERVAL_MILLIS)) {
                    saveCurrentProgress()
                    lastSaveTimeMillis = currentTime
                }
                delay(500L)
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
        player.setMediaItems(
            chapters.map {it.toMediaItem(book) }
        )

        player.prepare()
        Log.d("PlayerViewModel", "Loading")
        viewModelScope.launch {
            val savedProgress = repository.listenToReadingProgress(book.id).first()
            Log.d("PlayerViewModel", "$savedProgress")
            when (savedProgress) {
                is Resource.Success -> {
                    val savedProgress = savedProgress.data
                    val startChapter = savedProgress.currentChapter
                    val startPosition = TimeUnit.SECONDS.toMillis(savedProgress.currentPositionSecs)
                    player.seekTo(startChapter, startPosition)
                    playChapter(startChapter, context)
                    _uiState.update { it.copy(isLoading = false) }
                }
                is Resource.Error -> {
                    Log.w("PlayerViewModel", "No saved progress: ${savedProgress.throwable?.message}")
                    playChapter(0, context)
                    _uiState.update { it.copy(isLoading = false) }
                }
                null -> {
                    playChapter(0, context)
                    _uiState.update { it.copy(isLoading = false) }
                }

                Resource.Loading -> {_uiState.update { it.copy(isLoading = true) }}
            }
        }
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

    private fun togglePlayPause(context: Context){
        val isPlaying = player.isPlaying
        if(isPlaying) {
            player.pause()
        }else {
            startService(context)
            if (player.playbackState == Player.STATE_IDLE) {
                val currentIndex = _uiState.value.currentChapterIndex
                val currentPosition = _uiState.value.playbackPosition

                player.prepare()
                player.seekTo(currentIndex, currentPosition)
            }
            player.play()
        }
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

    fun saveCurrentProgress(){
        val book = _uiState.value.currentBook?:return
        if(player.currentMediaItem == null) return

        viewModelScope.launch {
            val totalDurationMillis = (0 until player.mediaItemCount).sumOf{
                index->
                player.getMediaItemAt(index).let {
                    val durationStr = _uiState.value.chapters.getOrNull(index)?.duration ?: "0"
                    parseTimeToMillis(durationStr)                }
            }
            val completedChaptersDuration = (0 until player.currentMediaItemIndex).sumOf { index ->
                val durationStr = _uiState.value.chapters.getOrNull(index)?.duration ?: "0"
                parseTimeToMillis(durationStr)
            }
            val elapsedMillis = completedChaptersDuration + player.currentPosition

            val progressPercentage = if (totalDurationMillis > 0) {
                ((elapsedMillis.toFloat() / totalDurationMillis) * 100).coerceIn(0f, 100f)
            } else {
                0f
            }

            val remainingMillis = totalDurationMillis - elapsedMillis
            val timeRemaining = formatTimeRemaining(remainingMillis)


            val progress = ReadingProgress(
                book = book,
                currentChapter = player.currentMediaItemIndex,
                totalChapters = book.numSections.toIntOrNull()?:player.mediaItemCount,
                currentPositionSecs = TimeUnit.MILLISECONDS.toSeconds(player.currentPosition),
                progressPercentage = progressPercentage,
                timeRemaining = timeRemaining,
                lastReadTimestamp = System.currentTimeMillis()
            )
            repository.updateReadingProgress(progress)
        }
    }

    private fun Chapter.toMediaItem(book: Book): MediaItem {
        val authors = book.authors.joinToString(", ") { "${it.firstName} ${it.lastName}" }
        return MediaItem.Builder()
            .setUri(this.audioUrl)
            .setMediaId(this.chapterNumber.toString())
            .setTag(this)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle("${book.title} - Chapter ${this.chapterNumber}")
                    .setArtist(authors)
                    .setArtworkUri(book.coverArt.toUri())
                    .build()
            )
            .build()
    }
    fun clearPlayer() {
        player.stop()
        player.clearMediaItems()
        _uiState.update {
            it.copy(currentBook = null)
        }
        progressJob?.cancel()
    }
    override fun onCleared() {
        super.onCleared()
    }

    private fun formatTimeRemaining(millis: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60

        return when {
            hours > 0 -> "${hours}h ${minutes}m remaining"
            minutes > 0 -> "${minutes}m remaining"
            else -> "Less than 1m remaining"
        }
    }
    private fun parseTimeToMillis(timeString: String): Long {
        return try {
            val cleaned = timeString.trim()
            val parts = cleaned.split(":")

            when (parts.size) {
                3 -> {
                    val hours = parts[0].toLongOrNull() ?: 0L
                    val minutes = parts[1].toLongOrNull() ?: 0L
                    val seconds = parts[2].toLongOrNull() ?: 0L

                    TimeUnit.HOURS.toMillis(hours) +
                            TimeUnit.MINUTES.toMillis(minutes) +
                            TimeUnit.SECONDS.toMillis(seconds)
                }
                2 -> {
                    val minutes = parts[0].toLongOrNull() ?: 0L
                    val seconds = parts[1].toLongOrNull() ?: 0L

                    TimeUnit.MINUTES.toMillis(minutes) +
                            TimeUnit.SECONDS.toMillis(seconds)
                }
                1 -> {
                    val seconds = parts[0].toLongOrNull() ?: 0L
                    TimeUnit.SECONDS.toMillis(seconds)
                }
                else -> 0L
            }
        } catch (e: Exception) {
            Log.e("PlayerViewModel", "Error parsing time: $timeString", e)
            0L
        }
    }
}