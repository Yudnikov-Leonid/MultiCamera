package com.multicamera.multicamera.chooseCamera.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.multicamera.multicamera.core.CameraManagerWrapper
import com.multicamera.multicamera.core.presentation.BundleWrapper
import com.multicamera.multicamera.core.presentation.Communication
import com.multicamera.multicamera.core.presentation.Init
import com.multicamera.multicamera.core.presentation.Navigation
import com.multicamera.multicamera.core.presentation.SaveAndRestore
import com.multicamera.multicamera.multiCamera.data.ShareCameraId
import com.multicamera.multicamera.multiCamera.presentation.MultiCameraScreen

class ChooseCameraViewModel(
    private val communication: ChooseCameraCommunication,
    private val shareCameraId: ShareCameraId.Update,
    private val navigation: Navigation.Update,
    private val cameraManagerWrapper: CameraManagerWrapper
) : ViewModel(), Communication.Observe<ChooseCameraState>, Init, SaveAndRestore {
    private val cameraNames = mutableListOf<String>()

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            cameraNames.clear()
            cameraNames.addAll(cameraManagerWrapper.cameras().map {
                "$it (${if (cameraManagerWrapper.isFront(it)) "FRONT" else "BACK"})"
            })
            communication.update(ChooseCameraState.Initial(cameraNames))
        }
    }

    fun choose(index: Int) {
        val physicalCameras =
            cameraManagerWrapper.physicalCameras(cameraManagerWrapper.cameras()[index])
        if (physicalCameras.isEmpty())
            communication.update(
                ChooseCameraState.Base(
                    cameraNames,
                    "Camera ${cameraManagerWrapper.cameras()[index]} does not have supported physical cameras"
                )
            )
        else {
            communication.update(ChooseCameraState.Initial(cameraNames))
            shareCameraId.saveLogical(cameraManagerWrapper.cameras()[index])
            navigation.update(MultiCameraScreen)
        }
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<ChooseCameraState>) {
        communication.observe(owner, observer)
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        communication.save(KEY, bundleWrapper)
        bundleWrapper.save(NAMES_KEY, cameraNames.toTypedArray())
        shareCameraId.save(bundleWrapper)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        communication.restore(KEY, bundleWrapper)
        cameraNames.clear()
        cameraNames.addAll(bundleWrapper.restore<Array<String>>(NAMES_KEY)!!.toList())
        shareCameraId.restore(bundleWrapper)
    }

    companion object {
        private const val KEY = "choose_camera_viewmodel_restore_key"
        private const val NAMES_KEY = "choose_camera_viewmodel_names_restore_key"
    }
}