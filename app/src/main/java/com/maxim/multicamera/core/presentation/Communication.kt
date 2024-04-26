package com.maxim.multicamera.core.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

interface Communication {
    interface Update<T> {
        fun update(value: T)
    }

    interface Observe<T> {
        fun observe(owner: LifecycleOwner, observer: Observer<T>)
    }

    interface ObserveForever<T> {
        fun observe(observer: Observer<T>)
        fun stopObserve(observer: Observer<T>)
    }

    interface Mutable<T>: Update<T>, Observe<T>

    abstract class Abstract<T>(protected val liveData: MutableLiveData<T>): Mutable<T> {
        override fun update(value: T) {
            liveData.value = value
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
            liveData.observe(owner, observer)
        }
    }

    abstract class AbstractForever<T>(protected val liveData: MutableLiveData<T>): Update<T>,
        ObserveForever<T> {
        override fun update(value: T) {
            liveData.value = value
        }

        override fun observe(observer: Observer<T>) {
            liveData.observeForever(observer)
        }

        override fun stopObserve(observer: Observer<T>) {
            liveData.removeObserver(observer)
        }
    }

    abstract class Single<T>: Abstract<T>(SingleLiveEvent())
    abstract class Regular<T>: Abstract<T>(MutableLiveData())

    abstract class SingleForever<T>: AbstractForever<T>(SingleLiveEvent())
    abstract class RegularForever<T>: AbstractForever<T>(MutableLiveData())
}