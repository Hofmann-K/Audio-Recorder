package com.audiorecorder

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.audiorecorder.playback.AndroidAudioPlayer
import com.audiorecorder.record.AndroidAudioRecorder
import com.audiorecorder.ui.theme.AudioRecorderTheme
import java.io.File
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )
        setContent {
            AudioRecorderTheme {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                File(cacheDir, "audio.mp3").also {
                                    recorder.start(it)
                                    audioFile = it
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_start_recording),
                                contentDescription = "Start recording",
                                // modifier = Modifier.size(48.dp)
                            )
                        }
                        Button(
                            onClick = {
                                recorder.stop()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_stop_recording),
                                contentDescription = "Stop recording",
                                // modifier = Modifier.size(48.dp)
                            )
                        }
                        Button(
                            onClick = {
                                player.playFile(audioFile ?: return@Button)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_play_audio),
                                contentDescription = "Play",
                                // modifier = Modifier.size(48.dp)
                            )
                        }
                        Button(
                            onClick = {
                                player.stop()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_pause_audio),
                                contentDescription = "Stop playing"
                            )
                            // modifier = Modifier.size(48.dp)
                        }
                    }
                }
            }
        }
    }
}