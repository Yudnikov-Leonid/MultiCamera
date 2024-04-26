package com.maxim.multicamera.core.sl

import android.content.Context
import android.hardware.camera2.CameraManager
import com.maxim.multicamera.core.CameraManagerWrapper
import com.maxim.multicamera.core.presentation.Navigation

interface Core : ProvideNavigation, ProvideCameraManagerWrapper {

    class Base(private val context: Context) : Core {

        private val navigation = Navigation.Base()
        override fun navigation() = navigation

        private val cameraManagerWrapper =
            CameraManagerWrapper.Base(context.getSystemService(Context.CAMERA_SERVICE) as CameraManager)
        override fun camaraManagerWrapper() = cameraManagerWrapper
    }
}

interface ProvideNavigation {
    fun navigation(): Navigation.Mutable
}

interface ProvideCameraManagerWrapper {
    fun camaraManagerWrapper(): CameraManagerWrapper
}