package com.maxim.multicamera.core.sl

import androidx.lifecycle.ViewModel
import com.maxim.multicamera.camera.CameraModule
import com.maxim.multicamera.camera.presentation.CameraViewModel
import com.maxim.multicamera.chooseCamera.ChooseCameraModule
import com.maxim.multicamera.chooseCamera.presentation.ChooseCameraViewModel
import com.maxim.multicamera.main.MainModule
import com.maxim.multicamera.main.MainViewModel
import com.maxim.multicamera.multiCamera.MultiCameraModule
import com.maxim.multicamera.multiCamera.presentation.MultiCameraViewModel

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