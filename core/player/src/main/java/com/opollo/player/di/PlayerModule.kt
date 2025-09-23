package com.opollo.player.di

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Provides
    @Singleton
    fun provideExoplayer(@ApplicationContext context: Context): ExoPlayer{
        return ExoPlayer.Builder(context).build()
    }

    @Provides
    @Singleton
    fun provideMediaSession(
        @ApplicationContext context: Context,
        player: ExoPlayer,
        @SessionActivityPendingIntent pendingIntent: PendingIntent
    ): MediaSession{
        return MediaSession.Builder(context,player)
            .setSessionActivity(pendingIntent)
            .build()
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SessionActivityPendingIntent