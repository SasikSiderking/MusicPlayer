package com.example.musicplayer

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MusicService : Service() {

    private val binder: IBinder = MusicServiceBinder()

    val musicPlayerState: LiveData<Boolean>
        get() = _musicPlayerState
    private val _musicPlayerState: MutableLiveData<Boolean> = MutableLiveData()

    private lateinit var mediaSession: MediaSessionCompat

    var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, MUSIC_SESSION_TAG)
        return binder
    }

    fun start() {
        mediaPlayer?.start()
    }

    fun stop() {
        mediaPlayer?.stop()
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun release() {
        mediaPlayer?.release()
    }

    fun reset() {
        mediaPlayer?.reset()
    }

    fun isPlaying() {
        mediaPlayer?.isPlaying
    }

    fun setDataSource(path: String) {
        mediaPlayer?.setDataSource(path)
    }

    fun prepare() {
        mediaPlayer?.prepare()
    }

    fun showNotification() {

        val intentPrevious = Intent(baseContext, NotificationActionReceiver::class.java).setAction(
            NotificationActions.Previous.name
        )
        val pendingIntentPrevious = PendingIntent.getBroadcast(
            baseContext,
            0,
            intentPrevious,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intentPlay = Intent(baseContext, NotificationActionReceiver::class.java).setAction(
            NotificationActions.Play.name
        )
        val pendingIntentPlay = PendingIntent.getBroadcast(
            baseContext,
            0,
            intentPlay,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intentNext = Intent(baseContext, NotificationActionReceiver::class.java).setAction(
            NotificationActions.Next.name
        )
        val pendingIntentNext = PendingIntent.getBroadcast(
            baseContext,
            0,
            intentNext,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intentClose = Intent(baseContext, NotificationActionReceiver::class.java).setAction(
            NotificationActions.Close.name
        )
        val pendingIntentClose = PendingIntent.getBroadcast(
            baseContext,
            0,
            intentClose,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(baseContext, CHANNEL_ID)
            .setContentTitle("Song")
            .setContentText("Rammstein")
            .setSmallIcon(R.drawable.baseline_queue_music_24)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.sessionToken)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(
                R.drawable.baseline_skip_previous_24,
                NotificationActions.Previous.name,
                pendingIntentPrevious
            )
            .addAction(
                R.drawable.baseline_play_arrow_24,
                NotificationActions.Play.name,
                pendingIntentPlay
            )
            .addAction(
                R.drawable.baseline_skip_next_24,
                NotificationActions.Next.name,
                pendingIntentNext
            )
            .addAction(
                R.drawable.baseline_close_24,
                NotificationActions.Close.name,
                pendingIntentClose
            )
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun initMediaPlayer(){
        reset()
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(this,R.raw.can_you_feel_my_heart)
        }
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService{
            return this@MusicService
        }
    }

    companion object {
        const val CHANNEL_DESCRIPTION = "Channel for music"
        const val CHANNEL_NAME = "Music chanmel"
        const val CHANNEL_ID = "musicChannel"
        const val NOTIFICATION_ID = 993
        const val MUSIC_SESSION_TAG = "Music Session"
    }
}