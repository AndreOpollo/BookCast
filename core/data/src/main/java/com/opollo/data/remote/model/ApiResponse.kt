package com.opollo.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val books: List<Book>
)


@Serializable
data class Book(
    val id:String,
    val title:String,
    val description:String,
    val language: String,
    val authors:List<Author>,
    @SerialName("copyright_year")
    val copyrightYear:String,
    @SerialName("num_sections")
    val numSections:String,
    val totalTime:String,
    val totalTimeSecs:Int,
    @SerialName("url_rss")
    val urlRss:String,
    @SerialName("url_zip_file")
    val urlZipFile: String
)

@Serializable
data class Author(
    val id:Int,
    @SerialName("first_name")
    val firstName:String,
    @SerialName("last_name")
    val lastName:String
)

