package com.maxim.multicamera.multiCamera.presentation

import android.widget.Button
import com.maxim.multicamera.chooseCamera.presentation.AddButton

interface MultiCameraState {

    fun show(addButton: AddButton, startButton: Button)

    class Base(private val physicalNames: List<String>, private val canStart: Boolean): MultiCameraState {
        override fun show(addButton: AddButton, startButton: Button) {
            addButton.clear()
            physicalNames.forEach { addButton.addButton(it) }
            startButton.isEnabled = canStart
        }
    }
}