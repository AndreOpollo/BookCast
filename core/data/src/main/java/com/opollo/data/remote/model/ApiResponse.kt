package com.opollo.data.remote.model



import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val books: List<BookDto>
)


@Serializable
data class BookDto(
    val id:String,
    val title:String,
    val description:String,
    val language: String,
    val authors:List<AuthorDto>,
    @SerialName("copyright_year")
    val copyrightYear:String = "",
    @SerialName("num_sections")
    val numSections:String,
    @SerialName("totaltime")
    val totalTime:String,
    @SerialName("totaltimesecs")
    val totalTimeSecs:Int,
    @SerialName("url_rss")
    val urlRss:String,
    @SerialName("url_zip_file")
    val urlZipFile: String,
    @SerialName("coverart_jpg")
    val coverArt:String
)

@Serializable
data class AuthorDto(
    val id:String,
    @SerialName("first_name")
    val firstName:String,
    @SerialName("last_name")
    val lastName:String
)

