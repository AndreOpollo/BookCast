package com.opollo.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("books")
data class BookEntity(
    @PrimaryKey val id:String,
    val title:String,
    val description:String,
    val copyrightYear: String,
    val language:String,
    val numSections: String,
    val totalTime: String,
    val totalTimeSecs: Int,
    val urlRss: String,
    val urlZipFile: String,
    val genre:String? = null,
    val coverArt:String
)