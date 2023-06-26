package com.example.musicplayer

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.musicplayer.model.NotificationActions
import kotlin.system.exitProcess

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            NotificationActions.Previous.name -> {
                previous()
            }

            NotificationActions.Play.name -> {
                play()
            }

            NotificationActions.Next.name -> {
                next()
            }

            NotificationActions.Close.name -> {
                close()
            }
        }
    }

    private fun play() {
        val musicService = MusicService.getInstance()
        if (musicService.isPlaying() == true) {
            musicService.pause(R.drawable.baseline_play_arrow_24)
        } else {
            musicService.start(R.drawable.baseline_pause_24)
        }
    }

    private fun close() {
        val musicService = MusicService.getInstance()
        musicService.release()
        musicService.stopForeground(Service.STOP_FOREGROUND_REMOVE)
        exitProcess(1)
    }

    private fun next() {
        val musicService = MusicService.getInstance()
        musicService.next(R.drawable.baseline_pause_24)
    }

    private fun previous() {
        val musicService = MusicService.getInstance()
        musicService.previous(R.drawable.baseline_pause_24)
    }
}