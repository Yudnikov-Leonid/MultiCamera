package com.maxim.multicamera.chooseCamera.presentation

import android.view.View
import android.widget.TextView
import java.io.Serializable

interface ChooseCameraState: Serializable {
    fun show(addButton: AddButton, failTextView: TextView)

    class Initial(private val camerasNames: List<String>): ChooseCameraState {
        override fun show(addButton: AddButton, failTextView: TextView) {
            addButton.makeButtons(camerasNames)
            failTextView.visibility = View.GONE
        }
    }

    class Base(private val camerasNames: List<String>, private val failText: String) : ChooseCameraState {
        override fun show(addButton: AddButton, failTextView: TextView) {
            addButton.makeButtons(camerasNames)
            if (failText.isEmpty()) {
                failTextView.visibility = View.GONE
            } else {
                failTextView.visibility = View.VISIBLE
                failTextView.text = failText
            }
        }
    }
}