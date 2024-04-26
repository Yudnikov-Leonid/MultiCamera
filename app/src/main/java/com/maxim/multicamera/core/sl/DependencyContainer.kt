package com.maxim.multicamera.core.sl

import androidx.lifecycle.ViewModel

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
                else -> next.module(clasz)
            } as Module<T>
        }
    }
}