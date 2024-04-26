package com.maxim.multicamera.multiCamera.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.multicamera.core.CameraManagerWrapper
import com.maxim.multicamera.core.presentation.Communication
import com.maxim.multicamera.core.presentation.Init
import com.maxim.multicamera.core.presentation.Navigation
import com.maxim.multicamera.core.sl.ClearViewModel
import com.maxim.multicamera.multiCamera.data.ShareCameraId

class MultiCameraViewModel(
    private val communication: MultiCameraCommunication,
    private val shareCameraId: ShareCameraId.Read,
    private val cameraManagerWrapper: CameraManagerWrapper,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
) : ViewModel(), Communication.Observe<MultiCameraState>, Init {
    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(MultiCameraState.Base(cameraManagerWrapper.physicalCameras(shareCameraId.read())))
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<MultiCameraState>) {
        communication.observe(owner, observer)
    }
}