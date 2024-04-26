package com.maxim.multicamera.camera.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.multicamera.core.presentation.Communication
import com.maxim.multicamera.core.presentation.Init

class CameraViewModel(
    private val communication: CameraCommunication
): ViewModel(), Communication.Observe<CameraState>, Init {

    override fun init(isFirstRun: Boolean) {

    }

    override fun observe(owner: LifecycleOwner, observer: Observer<CameraState>) {
        communication.observe(owner, observer)
    }
}