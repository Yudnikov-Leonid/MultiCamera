package com.maxim.multicamera.chooseCamera.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.multicamera.core.CameraManagerWrapper
import com.maxim.multicamera.core.presentation.Communication
import com.maxim.multicamera.core.presentation.Init

class ChooseCameraViewModel(
    private val communication: ChooseCameraCommunication,
    private val cameraManagerWrapper: CameraManagerWrapper
) : ViewModel(), Communication.Observe<ChooseCameraState>, Init {
    private val cameraNames = mutableListOf<String>()

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            cameraNames.addAll(cameraManagerWrapper.cameras().map {
                "$it (${if (cameraManagerWrapper.isFront(it)) "FRONT" else "BACK"})"
            })
            communication.update(ChooseCameraState.Initial(cameraNames))
        }
    }

    fun choose(index: Int) {
        val physicalCameras = cameraManagerWrapper.physicalCameras(cameraManagerWrapper.cameras()[index])
        if (physicalCameras.isEmpty())
            communication.update(ChooseCameraState.Base(cameraNames, "Camera does not have supported physical cameras"))
        else {
            communication.update(ChooseCameraState.Initial(cameraNames))
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<ChooseCameraState>) {
        communication.observe(owner, observer)
    }
}