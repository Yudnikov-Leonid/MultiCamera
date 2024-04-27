package com.multicamera.multicamera.multiCamera.data

import com.multicamera.multicamera.core.presentation.BundleWrapper
import com.multicamera.multicamera.core.presentation.SaveAndRestore

interface ShareCameraId {
    interface Update: SaveAndRestore {
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

        override fun save(bundleWrapper: BundleWrapper.Save) {
            bundleWrapper.save(ID_KEY, id)
            bundleWrapper.save(PHYSICAL_KEY, physical)
        }

        override fun restore(bundleWrapper: BundleWrapper.Restore) {
            id = bundleWrapper.restore(ID_KEY)!!
            physical = bundleWrapper.restore(PHYSICAL_KEY)!!
        }

        override fun readLogical() = id
        override fun readPhysical() = physical

        companion object {
            private const val ID_KEY = "share_camera_id_restore_key"
            private const val PHYSICAL_KEY = "share_camera_physical_restore_key"
        }
    }
}