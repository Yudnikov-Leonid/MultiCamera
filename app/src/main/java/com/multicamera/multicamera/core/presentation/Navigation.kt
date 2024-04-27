package com.multicamera.multicamera.core.presentation

interface Navigation {
    interface Update: Communication.Update<Screen>
    interface Observe: Communication.Observe<Screen>
    interface Mutable: Update, Observe
    class Base: Mutable, Communication.Single<Screen>()
}