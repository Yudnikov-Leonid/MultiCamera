package com.multicamera.multicamera.chooseCamera.presentation

import com.multicamera.multicamera.core.presentation.Communication

interface ChooseCameraCommunication: Communication.All<ChooseCameraState> {
    class Base: ChooseCameraCommunication, Communication.RegularWithDeath<ChooseCameraState>()
}