package com.maxim.multicamera.multiCamera.data

interface ShareCameraId {
    interface Update {
        fun save(id: String)
    }

    interface Read {
        fun read(): String
    }

    interface Mutable: Update, Read

    class Base(): Mutable {
        private var id = ""

        override fun save(id: String) {
            this.id = id
        }

        override fun read() = id
    }
}