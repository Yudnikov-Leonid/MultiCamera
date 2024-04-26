package com.maxim.multicamera.chooseCamera

import com.maxim.multicamera.chooseCamera.presentation.ChooseCameraCommunication
import com.maxim.multicamera.chooseCamera.presentation.ChooseCameraViewModel
import com.maxim.multicamera.core.sl.Core
import com.maxim.multicamera.core.sl.Module

class ChooseCameraModule(private val core: Core) : Module<ChooseCameraViewModel> {
    override fun viewModel() =
        ChooseCameraViewModel(ChooseCameraCommunication.Base(), core.camaraManagerWrapper())
}