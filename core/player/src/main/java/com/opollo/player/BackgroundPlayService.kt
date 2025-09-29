package com.opollo.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.OptIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.core.app.NotificationCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.session.MediaStyleNotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BackgroundPlayService: MediaSessionService(){

    companion object{
        const val NOTIFICATION_ID = 10001
        const val NOTIFICATION_CHANNEL_ID = "background_playback_service"
        const val NOTIFICATION_CHANNEL_NAME = "background_playback"
        const val ACTION_PLAY = "action_play"
        const val ACTION_PAUSE = "action_pause"
        const val ACTION_NEXT  = "action_next"
        const val ACTION_PREVIOUS = "action_previous"
    }

    @Inject
    lateinit var player: ExoPlayer
    @Inject
    lateinit var mediaSession: MediaSession


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        player.addListener(object: Player.Listener{
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                updateNotification()
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                updateNotification()
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        startForeground(NOTIFICATION_ID,createNowPlayingNotification())
        when(intent?.action){
            ACTION_PLAY -> player.play()
            ACTION_PAUSE ->player.pause()
            ACTION_PREVIOUS->skipToPrevious()
            ACTION_NEXT->skipToNext()
        }
        updateNotification()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.release()
        player.release()
    }
    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    @OptIn(UnstableApi::class)
    private fun createNowPlayingNotification(): Notification{
        val title = player.currentMediaItem?.mediaMetadata?.title ?: "Now Playing"
        val author = player.currentMediaItem?.mediaMetadata?.artist ?:"Unknown Author"
        val playPauseAction = if(player.isPlaying){
            NotificationCompat.Action.Builder(
                androidx.media3.session.R.drawable.media3_icon_pause,
                "Pause",
                createPendingIntent(ACTION_PAUSE)
            ).build()
        }else {
            NotificationCompat.Action.Builder(
                androidx.media3.session.R.drawable.media3_icon_play,
                "Play",
                createPendingIntent(ACTION_PLAY)
            ).build()
        }
        val playNextAction = NotificationCompat.Action.Builder(
            androidx.media3.session.R.drawable.media3_icon_next,
            "Next",
            createPendingIntent(ACTION_NEXT)
        ).build()
        val playPreviousAction = NotificationCompat.Action.Builder(
            androidx.media3.session.R.drawable.media3_icon_previous,
            "Previous",
            createPendingIntent(ACTION_PREVIOUS)
        ).build()

        return NotificationCompat.Builder(
            this,
            NOTIFICATION_CHANNEL_ID
        )
            .setContentTitle(title)
            .setContentText(author)
            .setSmallIcon(androidx.media3.session.R.drawable.media3_notification_small_icon)
            .setContentIntent(mediaSession.sessionActivity)
            .addAction(playPreviousAction)
            .addAction(playPauseAction)
            .addAction(playNextAction)
            .setStyle(MediaStyleNotificationHelper.MediaStyle(mediaSession)
                .setShowActionsInCompactView(0,1,2))
            .setOnlyAlertOnce(true)
            .setOngoing(player.isPlaying)
            .setShowWhen(false)
            .build()

    }
    private fun updateNotification(){
        val notification = createNowPlayingNotification()
        startForeground(NOTIFICATION_ID,notification)
    }

    private fun createPendingIntent(action:String): PendingIntent{
        val intent = Intent(this,
            BackgroundPlayService::class.java).apply {
                this.action = action
        }
        return PendingIntent.getService(
            this,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun skipToNext(){
        if(player.hasNextMediaItem()){
            player.seekToNext()
            player.play()
        }
    }
    private fun skipToPrevious(){
        if(player.hasPreviousMediaItem()){
            player.seekToPrevious()
            player.play()
        }
    }
}