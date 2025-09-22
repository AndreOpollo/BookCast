package com.opollo.domain.model


import kotlinx.serialization.Serializable


@Serializable
data class Book(
    val id:String,
    val title:String,
    val description:String,
    val language: String,
    val authors:List<Author>,
    val copyrightYear:String,
    val numSections:String,
    val totalTime:String,
    val totalTimeSecs:Int,
    val urlRss:String,
    val urlZipFile: String,
    val coverArt:String
)

@Serializable
data class Author(
    val id:String,
    val firstName:String,
    val lastName:String
)

@Serializable
data class Chapter(
    val title:String,
    val audioUrl:String,
    val duration:String,
    val chapterNumber:Int
)