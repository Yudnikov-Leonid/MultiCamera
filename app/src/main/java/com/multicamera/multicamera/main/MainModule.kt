package com.multicamera.multicamera.main

import com.multicamera.multicamera.core.sl.Core
import com.multicamera.multicamera.core.sl.Module

class MainModule(private val core: Core): Module<MainViewModel> {
    override fun viewModel() = MainViewModel(core.navigation())
}