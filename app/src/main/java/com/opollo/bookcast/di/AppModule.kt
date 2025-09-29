package com.opollo.bookcast.di

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.opollo.bookcast.MainActivity
import com.opollo.player.di.SessionActivityPendingIntent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    @SessionActivityPendingIntent
    fun provideSessionActivityPendingIntent(
        @ApplicationContext context: Context
    ): PendingIntent{
        val intent = Intent(context,
            MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    }
}