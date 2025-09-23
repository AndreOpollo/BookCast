package com.opollo.player.presentation

import com.opollo.domain.model.Book
import com.opollo.domain.model.Chapter

data class PlayerUiState(
    val isPlaying: Boolean = false,
    val currentBook: Book? = null,
    val currentChapter: Chapter? = null,
    val chapters: List<Chapter> = emptyList(),
    val currentChapterIndex: Int = 0,
    val playbackPosition:Long = 0L,
    val bufferedPosition:Long = 0L,
    val playbackSpeed:Float = 1.0f,
    val errorMsg:String? = null,
    val isLoading:Boolean = false


)