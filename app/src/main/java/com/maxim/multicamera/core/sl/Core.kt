package com.maxim.multicamera.core.sl

import android.content.Context
import com.maxim.multicamera.core.presentation.Navigation

interface Core : ProvideNavigation {

    class Base(private val context: Context) : Core {

        private val navigation = Navigation.Base()
        override fun navigation() = navigation
    }
}

interface ProvideNavigation {
    fun navigation(): Navigation.Mutable
}