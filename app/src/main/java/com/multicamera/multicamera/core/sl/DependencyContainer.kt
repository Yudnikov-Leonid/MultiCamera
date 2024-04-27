package com.multicamera.multicamera.core.sl

import androidx.lifecycle.ViewModel
import com.multicamera.multicamera.camera.CameraModule
import com.multicamera.multicamera.camera.presentation.CameraViewModel
import com.multicamera.multicamera.chooseCamera.ChooseCameraModule
import com.multicamera.multicamera.chooseCamera.presentation.ChooseCameraViewModel
import com.multicamera.multicamera.main.MainModule
import com.multicamera.multicamera.main.MainViewModel
import com.multicamera.multicamera.multiCamera.MultiCameraModule
import com.multicamera.multicamera.multiCamera.presentation.MultiCameraViewModel

interface DependencyContainer {
    fun <T: ViewModel> module(clasz: Class<out T>): Module<T>

    class Error: DependencyContainer {
        override fun <T : ViewModel> module(clasz: Class<out T>): Module<T> {
            throw IllegalStateException("Unknown viewModel: $clasz")
        }
    }

    class Base(private val core: Core, private val clear: ClearViewModel, private val next: DependencyContainer = Error()):
        DependencyContainer {
        override fun <T : ViewModel> module(clasz: Class<out T>): Module<T> {
            return when(clasz) {
                MainViewModel::class.java -> MainModule(core)
                ChooseCameraViewModel::class.java -> ChooseCameraModule(core)
                MultiCameraViewModel::class.java -> MultiCameraModule(core, clear)
                CameraViewModel::class.java -> CameraModule(core, clear)
                else -> next.module(clasz)
            } as Module<T>
        }
    }
}