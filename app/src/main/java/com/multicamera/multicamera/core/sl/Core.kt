package com.multicamera.multicamera.core.sl

import android.content.Context
import android.hardware.camera2.CameraManager
import com.multicamera.multicamera.core.CameraManagerWrapper
import com.multicamera.multicamera.core.presentation.Navigation
import com.multicamera.multicamera.multiCamera.data.ShareCameraId

interface Core : ProvideNavigation, ProvideCameraManagerWrapper, ProvideShareCameraId {

    class Base(private val context: Context) : Core {

        private val navigation = Navigation.Base()
        override fun navigation() = navigation

        private val cameraManagerWrapper =
            CameraManagerWrapper.Base(context.getSystemService(Context.CAMERA_SERVICE) as CameraManager)
        override fun camaraManagerWrapper() = cameraManagerWrapper

        private val shareCameraId = ShareCameraId.Base()
        override fun shareCameraId() = shareCameraId
    }
}

interface ProvideNavigation {
    fun navigation(): Navigation.Mutable
}

interface ProvideCameraManagerWrapper {
    fun camaraManagerWrapper(): CameraManagerWrapper
}

interface ProvideShareCameraId {
    fun shareCameraId(): ShareCameraId.Mutable
}