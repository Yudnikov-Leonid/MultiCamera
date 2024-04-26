package com.maxim.multicamera.main

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.multicamera.chooseCamera.presentation.ChooseCameraScreen
import com.maxim.multicamera.core.presentation.Communication
import com.maxim.multicamera.core.presentation.Init
import com.maxim.multicamera.core.presentation.Navigation
import com.maxim.multicamera.core.presentation.Screen

class MainViewModel(
    private val navigation: Navigation.Mutable
): ViewModel(), Communication.Observe<Screen>, Init {
    override fun init(isFirstRun: Boolean) {
        if (isFirstRun)
            navigation.update(ChooseCameraScreen)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<Screen>) {
        navigation.observe(owner, observer)
    }
}