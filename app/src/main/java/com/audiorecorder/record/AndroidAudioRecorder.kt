package com.audiorecorder.record
// Declares the package name for the class.

import android.content.Context
import android.media.*
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
// Imports necessary Android classes and packages.

class AndroidAudioRecorder(private val context: Context) : AudioRecorder {

    private var executor: ExecutorService? = null
    private var recorder: AudioRecord? = null
    private var audioTrack: AudioTrack? = null
    private var isRecording = false
    // Declares private variables for handling recording functionality.

    override fun start(outputFile: File) {

        executor = Executors.newSingleThreadExecutor() // Initializes a single-threaded executor service

        executor?.execute { // Executes the following code block in the executor

            // Calculates the minimum buffer size required for recording audio
            try {
                val bufferSize = AudioRecord.getMinBufferSize(
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT
                )

                // Initializes an AudioRecord instance for recording audio from the microphone
                recorder = AudioRecord(
                    MediaRecorder.AudioSource.MIC,
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize
                )

                // Initializes an AudioTrack instance for playback of recorded audio
                audioTrack = AudioTrack(
                    AudioManager.STREAM_VOICE_CALL,
                    SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSize,
                    AudioTrack.MODE_STREAM
                )

                recorder?.startRecording()
                audioTrack?.play() // Starts recording and playback

                isRecording = true // Sets the recording flag to true

                FileOutputStream(outputFile).use { outputStream -> // Opens a FileOutputStream for writing audio data to the output file

                    val audioData = ByteArray(bufferSize) // Initializes a ByteArray for storing audio data

                    while (isRecording) { // Enters a loop for continuous recording

                        val bytesRead = recorder?.read(audioData, 0, bufferSize) ?: 0 // Reads audio data from the AudioRecord instance

                        if (bytesRead != AudioRecord.ERROR_INVALID_OPERATION) { // Checks if audio data is valid

                            outputStream.write(audioData, 0, bytesRead) // Writes audio data to the output file

                            audioTrack?.write(audioData, 0, bytesRead) // Writes audio data to the AudioTrack for playback
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace() // Handles exceptions by printing the stack trace
            }
        }
    }

    override fun stop() {

        // Shuts down the executor service immediately
        executor?.shutdownNow()

        isRecording = false

        // Stops and releases the AudioRecord instance
        recorder?.stop()
        recorder?.release()

        // Stops and releases the AudioTrack instance
        audioTrack?.stop()
        audioTrack?.release()
    }

    companion object {
        private const val SAMPLE_RATE = 44100 // Declares a companion object with a constant for the sample rate (Common sample rate for audio streaming)
    }
}