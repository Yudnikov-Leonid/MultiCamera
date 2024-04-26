package com.maxim.multicamera.core.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

abstract class BaseViewModel(private val runAsync: RunAsync): ViewModel() {
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    protected fun <T: Any> handle(background: suspend () -> T, ui: (T) -> Unit) {
        runAsync.handle(viewModelScope, background, ui)
    }

    protected fun <T: Any> handle(background: suspend () -> T) {
        runAsync.handle(viewModelScope, background, {})
    }
}