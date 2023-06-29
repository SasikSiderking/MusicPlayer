package com.example.musicplayer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.model.MusicPlayerState
import com.example.musicplayer.model.NotificationActions
import com.example.musicplayer.model.PlayButtonState

class MusicService : Service() {

    private val binder: IBinder = MusicServiceBinder()

    private var songIds: List<Int> = emptyList()

    private var position: Int = 1

    val musicPlayerState: LiveData<MusicPlayerState>
        get() = _musicPlayerState
    private val _musicPlayerState: MutableLiveData<MusicPlayerState> = MutableLiveData(
        MusicPlayerState(PlayButtonState.PLAY)
    )

    private lateinit var mediaSession: MediaSessionCompat

    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, MUSIC_SESSION_TAG)
        return binder
    }

    fun start(playIcon: Int) {
        showNotification(playIcon)
        mediaPlayer?.start()
        _musicPlayerState.postValue(musicPlayerState.value!!.copy(playButtonState = PlayButtonState.PAUSE))
    }

    fun pause(playIcon: Int) {
        showNotification(playIcon)
        mediaPlayer?.pause()
        _musicPlayerState.postValue(musicPlayerState.value!!.copy(playButtonState = PlayButtonState.PLAY))
    }

    fun release() {
        mediaPlayer?.release()
    }

    fun isPlaying(): Boolean? {
        return mediaPlayer?.isPlaying
    }

    fun next(playIcon: Int) {
        if (position == songIds.size - 1) {
            position = -1
        }
        initMediaPlayer(++position)
        start(playIcon)
    }

    fun previous(playIcon: Int) {
        if (position == 0) {
            position = songIds.size
        }
        initMediaPlayer(--position)
        start(playIcon)
    }

    private fun showNotification(playIcon: Int) {

        val intentActionsProvider = IntentActionsProvider(baseContext)

        val notification = NotificationCompat.Builder(baseContext, CHANNEL_ID)
            .setContentTitle("Song")
            .setContentText("Music is playing")
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
                intentActionsProvider.pendingIntentPrevious
            )
            .addAction(
                playIcon,
                NotificationActions.Play.name,
                intentActionsProvider.pendingIntentPlay
            )
            .addAction(
                R.drawable.baseline_skip_next_24,
                NotificationActions.Next.name,
                intentActionsProvider.pendingIntentNext
            )
            .addAction(
                R.drawable.baseline_close_24,
                NotificationActions.Close.name,
                intentActionsProvider.pendingIntentClose
            )
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    private fun initMediaPlayer(position: Int) {
        release()
        mediaPlayer = MediaPlayer.create(this, songIds[position])
    }

    inner class MusicServiceBinder : Binder() {
        fun getService(songIds: List<Int>): MusicService {
            if (instance == null) {
                this@MusicService.songIds = songIds
                initMediaPlayer(position)
                instance = this@MusicService
            }
            return instance!!
        }
    }

    companion object {
        const val CHANNEL_DESCRIPTION = "Channel for music"
        const val CHANNEL_NAME = "Music chanmel"
        const val CHANNEL_ID = "musicChannel"
        const val NOTIFICATION_ID = 993
        const val MUSIC_SESSION_TAG = "Music Session"

        fun createIntent(context: Context): Intent {
            return Intent(context, MusicService::class.java)
        }

        @Volatile
        private var instance: MusicService? = null
        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: MusicService().also { instance = it }
            }
    }
}