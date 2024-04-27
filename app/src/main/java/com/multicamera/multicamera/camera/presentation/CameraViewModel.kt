package com.multicamera.multicamera.camera.presentation

import androidx.lifecycle.ViewModel
import com.multicamera.multicamera.core.presentation.GoBack
import com.multicamera.multicamera.core.presentation.Navigation
import com.multicamera.multicamera.core.presentation.Screen
import com.multicamera.multicamera.core.sl.ClearViewModel
import com.multicamera.multicamera.multiCamera.data.ShareCameraId

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