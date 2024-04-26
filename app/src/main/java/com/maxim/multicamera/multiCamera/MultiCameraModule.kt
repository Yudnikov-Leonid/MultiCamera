package com.maxim.multicamera.multiCamera

import com.maxim.multicamera.core.sl.ClearViewModel
import com.maxim.multicamera.core.sl.Core
import com.maxim.multicamera.core.sl.Module
import com.maxim.multicamera.multiCamera.presentation.MultiCameraCommunication
import com.maxim.multicamera.multiCamera.presentation.MultiCameraViewModel

class MultiCameraModule(private val core: Core, private val clearViewModel: ClearViewModel) :
    Module<MultiCameraViewModel> {
    override fun viewModel() = MultiCameraViewModel(
        MultiCameraCommunication.Base(),
        core.shareCameraId(),
        core.camaraManagerWrapper(),
        core.navigation(),
        clearViewModel
    )
}