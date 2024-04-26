package com.maxim.multicamera.camera.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.multicamera.core.presentation.Communication
import com.maxim.multicamera.core.presentation.Init
import com.maxim.multicamera.multiCamera.data.ShareCameraId

class CameraViewModel(
    private val shareCameraId: ShareCameraId.Read,
    private val communication: CameraCommunication
) : ViewModel(), Communication.Observe<CameraState>, Init {

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(CameraState.Base(shareCameraId.read()))
        }
    }

    fun physicalsIds(): Pair<String, String> {
        return Pair("12", "13")
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<CameraState>) {
        communication.observe(owner, observer)
    }
}