package com.maxim.multicamera.multiCamera.presentation

import android.widget.Button
import com.maxim.multicamera.chooseCamera.presentation.AddButton

interface MultiCameraState {

    fun show(addButton: AddButton, startButton: Button)

    class Initial(private val physicalNames: List<String>): MultiCameraState {
        override fun show(addButton: AddButton, startButton: Button) {
            addButton.clear()
            physicalNames.forEach { addButton.addButton(it) }
            startButton.isEnabled = false
        }
    }

    class Base(
        private val canStart: Boolean
    ) : MultiCameraState {
        override fun show(addButton: AddButton, startButton: Button) {
            startButton.isEnabled = canStart
        }
    }
}