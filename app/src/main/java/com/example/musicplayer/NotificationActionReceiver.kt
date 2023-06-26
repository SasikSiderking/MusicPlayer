package com.example.musicplayer

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import kotlin.system.exitProcess

class NotificationActionReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            NotificationActions.Previous.name -> {}
            NotificationActions.Play.name -> {

            }
            NotificationActions.Next.name -> {}
            NotificationActions.Close.name -> {
                MainActivity.musicService!!.stopForeground(Service.STOP_FOREGROUND_REMOVE)
                MainActivity.musicService = null
                exitProcess(1)
            }
        }
    }
}