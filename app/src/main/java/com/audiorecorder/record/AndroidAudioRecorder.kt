package com.audiorecorder.record
// Declares the package name for the class.

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.media.MediaRecorder
import java.io.File
import java.io.FileOutputStream
import android.Manifest
import android.content.pm.PackageManager
// Imports necessary Android classes and packages.

class AndroidAudioRecorder(private val context: Context) : AudioRecorder {

    private var recorder: MediaRecorder? = null

    override fun start(outputFile: File) {

        // Determines the audio source to be used based on Bluetooth connectivity
        val audioSource = if (isBluetoothAudioConnected()) {
            MediaRecorder.AudioSource.DEFAULT // Use Bluetooth microphone (Bluetooth mic should be given default attribute upon connection)
        } else {
            MediaRecorder.AudioSource.MIC // Fallback to device's built-in microphone
        }

        recorder = MediaRecorder().apply { // Initializes a new instance of MediaRecorder

            setAudioSource(audioSource) // Sets the audio source for the MediaRecorder

            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // Sets the output format for the MediaRecorder

            setAudioEncoder(MediaRecorder.AudioEncoder.AAC) // Sets the audio encoder for the MediaRecorder

            setOutputFile(FileOutputStream(outputFile).fd) // Sets the output file for the recorded audio

            prepare() // Prepares the MediaRecorder for recording

            start() // Starts the recording
        }
    }

    override fun stop() {

        recorder?.stop() // Stops the recording if the recorder instance is not null

        recorder?.release() // Releases the resources associated with the MediaRecorder if it is not null

        recorder = null // Sets the recorder instance to null
    }

    private fun isBluetoothAudioConnected(): Boolean {

        // Checks if the app has permission to access Bluetooth
        if (context.checkSelfPermission(Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            return false // Returns false if Bluetooth permission is not granted
        }

        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter() // Retrieves the default Bluetooth adapter

        // Returns true if a Bluetooth A2DP profile is connected, otherwise returns false
        return bluetoothAdapter?.getProfileConnectionState(BluetoothProfile.A2DP) == BluetoothAdapter.STATE_CONNECTED
    }
}