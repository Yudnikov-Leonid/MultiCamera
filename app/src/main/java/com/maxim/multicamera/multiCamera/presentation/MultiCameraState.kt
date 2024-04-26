package com.maxim.multicamera.multiCamera.presentation

import android.widget.Button
import java.io.Serializable

interface MultiCameraState: Serializable {

    fun show(addButton: MakeRadioButtons, startButton: Button)

    class Base(
        private val physicalNames: List<String>,
        private val selection: Pair<Int, Int>,
        private val canStart: Boolean
    ) : MultiCameraState {
        override fun show(addButton: MakeRadioButtons, startButton: Button) {
            addButton.makeButtons(physicalNames, selection)
            startButton.isEnabled = canStart
        }
    }
}