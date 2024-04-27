package com.multicamera.multicamera.chooseCamera

import com.multicamera.multicamera.chooseCamera.presentation.ChooseCameraCommunication
import com.multicamera.multicamera.chooseCamera.presentation.ChooseCameraViewModel
import com.multicamera.multicamera.core.sl.Core
import com.multicamera.multicamera.core.sl.Module

class ChooseCameraModule(private val core: Core) : Module<ChooseCameraViewModel> {
    override fun viewModel() =
        ChooseCameraViewModel(
            ChooseCameraCommunication.Base(),
            core.shareCameraId(),
            core.navigation(),
            core.camaraManagerWrapper(),
        )
}