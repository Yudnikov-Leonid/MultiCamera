package com.multicamera.multicamera.multiCamera

import com.multicamera.multicamera.core.sl.ClearViewModel
import com.multicamera.multicamera.core.sl.Core
import com.multicamera.multicamera.core.sl.Module
import com.multicamera.multicamera.multiCamera.presentation.MultiCameraCommunication
import com.multicamera.multicamera.multiCamera.presentation.MultiCameraViewModel

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