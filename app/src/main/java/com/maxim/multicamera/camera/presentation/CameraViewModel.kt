package com.maxim.multicamera.camera.presentation

import androidx.lifecycle.ViewModel
import com.maxim.multicamera.core.presentation.GoBack
import com.maxim.multicamera.core.presentation.Navigation
import com.maxim.multicamera.core.presentation.Screen
import com.maxim.multicamera.core.sl.ClearViewModel
import com.maxim.multicamera.multiCamera.data.ShareCameraId

class CameraViewModel(
    private val shareCameraId: ShareCameraId.Read,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
) : ViewModel(), GoBack {

    fun logicalId() = shareCameraId.readLogical()

    fun physicalsIds() = shareCameraId.readPhysical()

    override fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clear(CameraViewModel::class.java)
    }
}