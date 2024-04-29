package com.audiorecorder.record

import java.io.File

// Declares functions to be used in audio recording
interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}