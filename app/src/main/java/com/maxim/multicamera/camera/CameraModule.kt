package com.maxim.multicamera.camera

import com.maxim.multicamera.camera.presentation.CameraViewModel
import com.maxim.multicamera.core.sl.ClearViewModel
import com.maxim.multicamera.core.sl.Core
import com.maxim.multicamera.core.sl.Module

class CameraModule(private val core: Core, private val clearViewModel: ClearViewModel) :
    Module<CameraViewModel> {
    override fun viewModel() =
        CameraViewModel(core.shareCameraId(), core.navigation(), clearViewModel)
}