package com.maxim.multicamera.multiCamera.presentation

import com.maxim.multicamera.core.presentation.Communication

interface MultiCameraCommunication: Communication.Mutable<MultiCameraState> {
    class Base: MultiCameraCommunication, Communication.Regular<MultiCameraState>()
}