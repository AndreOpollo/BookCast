package com.opollo.data.remote.api

import com.opollo.data.remote.model.ApiResponse
import com.opollo.data.remote.model.RssFeed
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface BooksApiService {

    @GET("/api/feed/audiobooks")
    suspend fun getBooks(
        @Query("format") format:String = "json"
    ): ApiResponse

    @GET("/api/feed/audiobooks")
    suspend fun getBookById(
        @Query(value = "id") id:String,
        @Query("format") format:String = "json"
    ): ApiResponse

    @GET("/api/feed/audiobooks")
    suspend fun getBookByTitle(
        @Query("title") title:String,
        @Query("format") format: String = "json"
    ): ApiResponse

    @GET("/api/feed/audiobooks")
    suspend fun getBooksByGenre(
        @Query("genre") genre:String,
        @Query("format") format:String = "json"
    ): ApiResponse

    @GET
    suspend fun getBookChapters(@Url rssUrl:String): RssFeed
}