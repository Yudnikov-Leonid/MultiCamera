package com.multicamera.multicamera.camera

import com.multicamera.multicamera.camera.presentation.CameraViewModel
import com.multicamera.multicamera.core.sl.ClearViewModel
import com.multicamera.multicamera.core.sl.Core
import com.multicamera.multicamera.core.sl.Module

class CameraModule(private val core: Core, private val clearViewModel: ClearViewModel) :
    Module<CameraViewModel> {
    override fun viewModel() =
        CameraViewModel(core.shareCameraId(), core.navigation(), clearViewModel)
}