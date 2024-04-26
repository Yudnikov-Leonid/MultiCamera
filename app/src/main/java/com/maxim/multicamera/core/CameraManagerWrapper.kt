package com.maxim.multicamera.core

import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager

interface CameraManagerWrapper {
    fun isFront(id: String): Boolean
    fun cameras(): List<String>

    class Base(private val cameraManager: CameraManager) : CameraManagerWrapper {
        override fun isFront(id: String): Boolean {
            return cameraManager.getCameraCharacteristics(id)
                .get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
        }

        override fun cameras(): List<String> {
            return cameraManager.cameraIdList.toList()
        }
    }
}