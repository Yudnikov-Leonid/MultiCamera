package com.maxim.multicamera.camera.presentation

import com.maxim.multicamera.core.presentation.Communication

interface CameraCommunication: Communication.Mutable<CameraState> {
    class Base: CameraCommunication, Communication.Regular<CameraState>()
}