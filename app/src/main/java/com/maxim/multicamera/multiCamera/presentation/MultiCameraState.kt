package com.maxim.multicamera.multiCamera.presentation

import com.maxim.multicamera.chooseCamera.presentation.AddButton

interface MultiCameraState {

    fun show(addButton: AddButton)

    class Base(private val physicalNames: List<String>): MultiCameraState {
        override fun show(addButton: AddButton) {
            addButton.clear()
            physicalNames.forEach { addButton.addButton(it) }
        }
    }
}