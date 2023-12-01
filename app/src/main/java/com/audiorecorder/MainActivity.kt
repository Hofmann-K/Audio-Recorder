package com.audiorecorder

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
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
    private var isRecording by mutableStateOf(false) // Added variable to track recording status

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
        // Main UI structure starts here
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top, // Align children from the top
            horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
        ) {
            // Custom top bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp) // Height of the custom top bar
                    .background(MaterialTheme.colorScheme.primary), // Background color
                contentAlignment = Alignment.Center // Center content within the Box
            ) {
                Text(
                    text = "Audio Playback App Demo",
                    color = Color.White // White text color
                )
            }

            // Box for the image
            Box(
                modifier = Modifier
                    .size(300.dp) // Adjust size as needed
                    .padding(5.dp) // Add padding to separate from the top bar
            ) {
                Image(
                    painter = painterResource(id = R.drawable.memos),
                    contentDescription = "Memos Image",
                    modifier = Modifier
                        .fillMaxSize()
                        //.offset(y = (-2).dp)
                )
            }

            // Row of buttons
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = (-100).dp)
                    .weight(1f), // Occupy available space
                horizontalArrangement = Arrangement.SpaceEvenly, // Space between children
                verticalAlignment = Alignment.CenterVertically // Center vertically
            ) {
                // Button for starting recording
                Button(
                    onClick = {
                        File(cacheDir, "audio.mp3").also {
                            recorder.start(it) // Start recording
                            audioFile = it
                            isRecording = true // Update recording status
                        }
                    },
                    modifier = Modifier.weight(1f) // Occupy equal space with other buttons
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center // Center content vertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_start_recording),
                            contentDescription = "Start recording",
                            modifier = Modifier.size(48.dp) // Adjust size as needed
                        )
                        Text("Start Record", textAlign = TextAlign.Center)
                    }
                }

                // Button for stopping recording
                Button(
                    onClick = {
                        recorder.stop() // Stop recording
                        isRecording = false // Update recording status
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center // Center content vertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_stop_recording),
                            contentDescription = "Stop recording",
                            modifier = Modifier.size(48.dp) // Adjust size as needed
                        )
                        Text("Stop Record", textAlign = TextAlign.Center)
                    }
                }

                // Button for playing recorded audio
                Button(
                    onClick = {
                        player.playFile(audioFile ?: return@Button)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center // Center content vertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_play_audio),
                            contentDescription = "Play",
                            modifier = Modifier.size(48.dp) // Adjust size as needed
                        )
                        Text("Play Audio", textAlign = TextAlign.Center)
                    }
                }

                // Button for stopping audio playback
                Button(
                    onClick = {
                        player.stop() // Stop playing audio
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center // Center content vertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_pause_audio),
                            contentDescription = "Stop playing",
                            modifier = Modifier.size(48.dp) // Adjust size as needed
                        )
                        Text("Stop Audio", textAlign = TextAlign.Center)
                    }
                }
            }

            // Status text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp) // Adjust padding as needed
                    .offset(y = (-230).dp), // Adjust the offset to move the status text up
                contentAlignment = Alignment.Center
            ) {
                Text("Recording Status: ${if (isRecording) "Recording" else "Not Recording"}", textAlign = TextAlign.Center)
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