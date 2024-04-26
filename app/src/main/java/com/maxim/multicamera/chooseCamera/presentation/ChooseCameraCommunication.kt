package com.maxim.multicamera.chooseCamera.presentation

import com.maxim.multicamera.core.presentation.Communication

interface ChooseCameraCommunication: Communication.Mutable<ChooseCameraState> {
    class Base: ChooseCameraCommunication, Communication.Regular<ChooseCameraState>()
}