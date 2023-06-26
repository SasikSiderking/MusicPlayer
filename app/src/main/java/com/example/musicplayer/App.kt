package com.example.musicplayer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App: Application(){
    override fun onCreate() {
        super.onCreate()
        val notificationChannel = NotificationChannel(MusicService.CHANNEL_ID, MusicService.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.description = MusicService.CHANNEL_DESCRIPTION
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}