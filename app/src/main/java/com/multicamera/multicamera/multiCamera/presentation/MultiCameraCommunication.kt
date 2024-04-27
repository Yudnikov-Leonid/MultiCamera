package com.multicamera.multicamera.multiCamera.presentation

import com.multicamera.multicamera.core.presentation.Communication

interface MultiCameraCommunication: Communication.All<MultiCameraState> {
    class Base: MultiCameraCommunication, Communication.RegularWithDeath<MultiCameraState>()
}