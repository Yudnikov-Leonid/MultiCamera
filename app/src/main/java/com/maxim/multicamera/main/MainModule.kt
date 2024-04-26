package com.maxim.multicamera.main

import com.maxim.multicamera.core.sl.Core
import com.maxim.multicamera.core.sl.Module

class MainModule(private val core: Core): Module<MainViewModel> {
    override fun viewModel() = MainViewModel(core.navigation())
}