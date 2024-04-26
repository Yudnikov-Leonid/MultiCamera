package com.maxim.multicamera.multiCamera.presentation

import com.maxim.multicamera.core.presentation.Communication

interface MultiCameraCommunication: Communication.All<MultiCameraState> {
    class Base: MultiCameraCommunication, Communication.RegularWithDeath<MultiCameraState>()
}