package com.example.musicplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.musicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ServiceConnection {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private var playButtonState: PlayButtonState = PlayButtonState.PLAYING

    var musicService: MusicService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = Intent(this, MusicService::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)

        binding.buttonPrevious.setOnClickListener(buttonPreviousOnClickListener)
        binding.buttonPlay.setOnClickListener(buttonPlayOnClickListener)
        binding.buttonNext.setOnClickListener(buttonNextOnClickListener)
    }

    private val buttonPreviousOnClickListener = View.OnClickListener {

    }

    private val buttonPlayOnClickListener = View.OnClickListener {
        playButtonState = when(playButtonState){
            PlayButtonState.PLAYING -> {
                binding.buttonPlay.setImageResource(R.drawable.baseline_play_arrow_24)
                musicService?.pause()
                PlayButtonState.PAUSE
            }

            PlayButtonState.PAUSE -> {
                binding.buttonPlay.setImageResource(R.drawable.baseline_pause_24)
                musicService?.start()
                PlayButtonState.PLAYING
            }
        }
    }

    private val buttonNextOnClickListener = View.OnClickListener {

    }

    private fun initMediaPlayer(){
        if (musicService?.mediaPlayer == null){
            musicService?.mediaPlayer = MediaPlayer.create(this,R.raw.can_you_feel_my_heart)
        }
//        musicService?.reset()
//        musicService?.setDataSource()
//        musicService?.prepare()
        musicService?.start()
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MusicServiceBinder
        musicService = binder.getService()
        musicService!!.musicPlayerState.observe(this,musicPlayerStateObserver)
        initMediaPlayer()

        musicService!!.showNotification()
    }

    private val musicPlayerStateObserver = Observer<Boolean>{

    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }
}