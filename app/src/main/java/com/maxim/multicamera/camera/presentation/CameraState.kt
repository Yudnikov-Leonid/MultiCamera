package com.maxim.multicamera.camera.presentation

interface CameraState {
    fun show(cameraController: CameraController)

    class Base(private val cameraId: String) : CameraState {
        override fun show(cameraController: CameraController) {

        }
    }
}