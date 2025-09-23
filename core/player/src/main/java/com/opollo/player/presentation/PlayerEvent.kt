package com.opollo.player.presentation

import com.opollo.domain.model.Book
import com.opollo.domain.model.Chapter

sealed class PlayerEvent {
    data class LoadBook(val book: Book,val chapters:List<Chapter>): PlayerEvent()
    data class PlayChapter(val index:Int): PlayerEvent()
    data object PlayPause: PlayerEvent()
    data object PlayNext: PlayerEvent()
    data object PlayPrevious: PlayerEvent()
    data class SeekTo(val position:Long): PlayerEvent()
    data class ChangePlaybackSpeed(val speed:Float): PlayerEvent()
    data object Stop: PlayerEvent()
}