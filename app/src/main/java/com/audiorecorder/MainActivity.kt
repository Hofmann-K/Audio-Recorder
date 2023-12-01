package com.audiorecorder

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.audiorecorder.playback.AndroidAudioPlayer
import com.audiorecorder.record.AndroidAudioRecorder
import com.audiorecorder.ui.theme.AudioRecorderTheme
import java.io.File

class MainActivity : ComponentActivity() {

    private val recorder by lazy {
        AndroidAudioRecorder(applicationContext)
    }

    private val player by lazy {
        AndroidAudioPlayer(applicationContext)
    }

    private var audioFile: File? = null

    // Preview function for Jetpack Compose UI
    @Preview
    @Composable
    fun PreviewMainActivity() {
        AudioRecorderTheme {
            MainActivityContent()
        }
    }

    @Composable
    fun MainActivityContent() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Custom top bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp) // Adjust the height as needed
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Audio Playback App Demo",
                    color = Color.White
                )
            }

            // Rest of your content
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
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
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AudioRecorderTheme {
                MainActivityContent()
            }
        }
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )
    }
}
