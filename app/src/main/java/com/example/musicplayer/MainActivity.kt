package com.example.musicplayer

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.musicplayer.databinding.ActivityMainBinding
import com.example.musicplayer.model.MusicPlayerState
import com.example.musicplayer.model.PlayButtonState

class MainActivity : AppCompatActivity(), ServiceConnection {

    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() = _binding!!

    private var playButtonState: PlayButtonState = PlayButtonState.PLAY

    private val songIds: List<Int> = listOf(
        R.raw.everlasting_summer,
        R.raw.can_you_feel_my_heart,
        R.raw.gangstars_paradise,
        R.raw.give_me_my_dreams
    )

    var musicService: MusicService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = MusicService.createIntent(this)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)

        binding.buttonPrevious.setOnClickListener(buttonPreviousOnClickListener)
        binding.buttonPlay.setOnClickListener(buttonPlayOnClickListener)
        binding.buttonNext.setOnClickListener(buttonNextOnClickListener)
    }

    private val buttonPreviousOnClickListener = View.OnClickListener {
        musicService?.previous(R.drawable.baseline_pause_24)
    }

    private val buttonPlayOnClickListener = View.OnClickListener {
        when (playButtonState) {
            PlayButtonState.PLAY -> {
                musicService?.start(R.drawable.baseline_pause_24)
            }

            PlayButtonState.PAUSE -> {
                musicService?.pause(R.drawable.baseline_play_arrow_24)
            }
        }
    }

    private val buttonNextOnClickListener = View.OnClickListener {
        musicService?.next(R.drawable.baseline_pause_24)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MusicServiceBinder
        musicService = binder.getService(songIds)
        musicService!!.musicPlayerState.observe(this, musicPlayerStateObserver)
    }

    private val musicPlayerStateObserver = Observer<MusicPlayerState> {
        playButtonState = it.playButtonState
        when (it.playButtonState) {
            PlayButtonState.PLAY -> {
                binding.buttonPlay.setImageResource(R.drawable.baseline_play_arrow_24)
            }

            PlayButtonState.PAUSE -> {
                binding.buttonPlay.setImageResource(R.drawable.baseline_pause_24)
            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }
}