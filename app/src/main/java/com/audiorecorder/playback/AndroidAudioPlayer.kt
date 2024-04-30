package com.audiorecorder.playback
// Declares the package name for the class

import android.content.Context
import android.media.MediaPlayer
import androidx.core.net.toUri
import java.io.File
// Imports necessary Android classes and packages


class AndroidAudioPlayer(
    private val context: Context
): AudioPlayer {

    private var player: MediaPlayer? = null // Declares a nullable MediaPlayer variable

    override fun playFile(file: File) {

        MediaPlayer.create(context, file.toUri()).apply { // Creates a MediaPlayer instance using the context and file URI

            player = this // Assigns the created MediaPlayer instance to the player variable

            start() // Starts playback of the audio file
        }
    }

    override fun stop() {

        player?.stop() // Stops the MediaPlayer if the player instance is not null

        player?.release() // Releases the resources associated with the MediaPlayer if it is not null

        player = null // Sets the player instance to null
    }
}