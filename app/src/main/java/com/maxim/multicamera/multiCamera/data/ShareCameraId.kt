package com.maxim.multicamera.multiCamera.data

interface ShareCameraId {
    interface Update {
        fun saveLogical(id: String)
        fun savePhysical(ids: Pair<String, String>)
    }

    interface Read {
        fun readLogical(): String
        fun readPhysical(): Pair<String, String>
    }

    interface Mutable: Update, Read

    class Base: Mutable {
        private var id = ""
        private var physical = Pair("", "")

        override fun saveLogical(id: String) {
            this.id = id
        }

        override fun savePhysical(ids: Pair<String, String>) {
            physical = ids
        }

        override fun readLogical() = id
        override fun readPhysical() = physical
    }
}