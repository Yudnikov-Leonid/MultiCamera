package com.multicamera.multicamera.core

import android.app.Application
import androidx.lifecycle.ViewModel
import com.multicamera.multicamera.core.sl.ClearViewModel
import com.multicamera.multicamera.core.sl.Core
import com.multicamera.multicamera.core.sl.DependencyContainer
import com.multicamera.multicamera.core.sl.ProvideViewModel
import com.multicamera.multicamera.core.sl.ViewModelFactory

class App: Application(), ProvideViewModel {

    private lateinit var factory: ViewModelFactory

    override fun onCreate() {
        super.onCreate()

        val core = Core.Base(this)
        val dependencyContainer = DependencyContainer.Base(core, object : ClearViewModel {
            override fun clear(clasz: Class<out ViewModel>) {
                factory.clear(clasz)
            }
        })
        factory = ViewModelFactory.Base(ProvideViewModel.Base(dependencyContainer))
    }

    override fun <T : ViewModel> viewModel(clasz: Class<T>): T {
        return factory.viewModel(clasz)
    }
}