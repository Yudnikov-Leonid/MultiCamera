package com.maxim.multicamera.chooseCamera.presentation

import android.view.View
import android.widget.TextView

interface ChooseCameraState {
    fun show(addButton: AddButton, failTextView: TextView)

    class Initial(private val camerasNames: List<String>): ChooseCameraState {
        override fun show(addButton: AddButton, failTextView: TextView) {
            addButton.clear()
            camerasNames.forEach {
                addButton.addButton(it)
            }
        }
    }

    class Base(private val camerasNames: List<String>, private val failText: String) : ChooseCameraState {
        override fun show(addButton: AddButton, failTextView: TextView) {
            addButton.clear()
            camerasNames.forEach {
                addButton.addButton(it)
            }
            if (failText.isEmpty()) {
                failTextView.visibility = View.GONE
            } else {
                failTextView.visibility = View.VISIBLE
                failTextView.text = failText
            }
        }
    }
}