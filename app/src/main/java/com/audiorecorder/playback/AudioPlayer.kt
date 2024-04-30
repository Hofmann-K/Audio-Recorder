package com.audiorecorder.playback


import java.io.File

// Declares functions to be used in audio playback
interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}