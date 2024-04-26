package com.maxim.multicamera.chooseCamera.presentation

import com.maxim.multicamera.core.presentation.Communication

interface ChooseCameraCommunication: Communication.All<ChooseCameraState> {
    class Base: ChooseCameraCommunication, Communication.RegularWithDeath<ChooseCameraState>()
}