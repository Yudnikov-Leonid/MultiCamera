package com.multicamera.multicamera.core.sl

import androidx.lifecycle.ViewModel

interface ViewModelFactory: ProvideViewModel, ClearViewModel {

    class Base(private val createViewModel: ProvideViewModel): ViewModelFactory {
        private val map = mutableMapOf<Class<out ViewModel>, ViewModel>()

        override fun <T : ViewModel> viewModel(clasz: Class<T>): T {
            if (map[clasz] == null)
                map[clasz] = createViewModel.viewModel(clasz)
            return map[clasz] as T
        }

        override fun clear(clasz: Class<out ViewModel>) {
            map.remove(clasz)
        }
    }
}

interface ProvideViewModel {
    fun <T: ViewModel> viewModel(clasz: Class<T>): T

    class Base(private val dependencyContainer: DependencyContainer): ProvideViewModel {
        override fun <T : ViewModel> viewModel(clasz: Class<T>): T {
            return dependencyContainer.module(clasz).viewModel()
        }
    }
}

interface ClearViewModel {
    fun clear(clasz: Class<out ViewModel>)
}