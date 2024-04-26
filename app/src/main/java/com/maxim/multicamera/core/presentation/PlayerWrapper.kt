package com.maxim.multicamera.core.presentation

import android.content.Context
import android.media.MediaPlayer

interface PlayerWrapper {

    fun play(id: Int)

    class Base(private val context: Context): PlayerWrapper {
        private var mediaPlayer: MediaPlayer? = null

        override fun play(id: Int) {
            mediaPlayer?.let {
                it.stop()
                it.release()
            }
            mediaPlayer = MediaPlayer.create(context, id)
            mediaPlayer!!.start()
        }
    }
}