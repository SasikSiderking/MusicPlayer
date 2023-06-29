package com.example.musicplayer

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.musicplayer.model.NotificationActions

class IntentActionsProvider(baseContext: Context) {

    private val intentPrevious =
        Intent(baseContext, NotificationActionReceiver::class.java).setAction(
            NotificationActions.Previous.name
        )
    val pendingIntentPrevious: PendingIntent = PendingIntent.getBroadcast(
        baseContext,
        0,
        intentPrevious,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    private val intentPlay = Intent(baseContext, NotificationActionReceiver::class.java).setAction(
        NotificationActions.Play.name
    )
    val pendingIntentPlay: PendingIntent = PendingIntent.getBroadcast(
        baseContext,
        0,
        intentPlay,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    private val intentNext = Intent(baseContext, NotificationActionReceiver::class.java).setAction(
        NotificationActions.Next.name
    )
    val pendingIntentNext: PendingIntent = PendingIntent.getBroadcast(
        baseContext,
        0,
        intentNext,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    private val intentClose = Intent(baseContext, NotificationActionReceiver::class.java).setAction(
        NotificationActions.Close.name
    )
    val pendingIntentClose: PendingIntent = PendingIntent.getBroadcast(
        baseContext,
        0,
        intentClose,
        PendingIntent.FLAG_UPDATE_CURRENT
    )
}