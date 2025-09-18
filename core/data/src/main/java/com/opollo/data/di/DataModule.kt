package com.opollo.data.di

import android.content.Context
import androidx.activity.contextaware.ContextAware
import androidx.room.Room
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.opollo.data.local.db.BookDatabase
import com.opollo.data.remote.api.BooksApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    private const val BASE_URL = "https://librivox.org/"
    private const val RSS_BASE_URL = "https://librivox.org/rss/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
       return OkHttpClient.Builder()
           .addInterceptor(logging)
           .build()
    }

    @Provides
    @Singleton
    @JsonRetrofit
    fun provideJsonRetrofit(client: OkHttpClient): Retrofit{
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    @XmlRetrofit
    fun provideXmlRetrofit(client: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl(RSS_BASE_URL)
            .client(client)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @JsonRetrofit
    fun provideJsonBooksApi(@JsonRetrofit retrofit: Retrofit): BooksApiService {
        return retrofit.create(BooksApiService::class.java)
    }

    @Provides
    @Singleton
    @XmlRetrofit
    fun providesXmlBooksApi(@XmlRetrofit retrofit: Retrofit): BooksApiService{
        return retrofit.create(BooksApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideBookDatabase(@ApplicationContext context: Context): BookDatabase {
        return Room.databaseBuilder(
            context,
            BookDatabase::class.java,
            "book_database"
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideBookDao(db: BookDatabase) = db.bookDao()

    @Provides
    @Singleton
    fun provideAuthorDao(db: BookDatabase) = db.authorDao()

    @Provides
    @Singleton
    fun provideBookAuthorDao(db: BookDatabase) = db.bookAuthorDao()
}