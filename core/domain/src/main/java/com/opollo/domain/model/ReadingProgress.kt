package com.opollo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ReadingProgress(
    val book: Book = Book(),
    val currentChapter: Int = 0,
    val totalChapters:Int = 0,
    val currentPositionSecs:Long = 0L,
    val progressPercentage: Float = 0f,
    val timeRemaining:String = "",
    val isFavorite:Boolean = false,
    val lastReadTimestamp: Long = 0L
)