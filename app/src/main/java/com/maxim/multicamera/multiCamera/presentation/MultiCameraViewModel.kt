package com.maxim.multicamera.multiCamera.presentation

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.maxim.multicamera.camera.presentation.CameraScreen
import com.maxim.multicamera.core.CameraManagerWrapper
import com.maxim.multicamera.core.presentation.BundleWrapper
import com.maxim.multicamera.core.presentation.Communication
import com.maxim.multicamera.core.presentation.GoBack
import com.maxim.multicamera.core.presentation.Init
import com.maxim.multicamera.core.presentation.Navigation
import com.maxim.multicamera.core.presentation.SaveAndRestore
import com.maxim.multicamera.core.presentation.Screen
import com.maxim.multicamera.core.sl.ClearViewModel
import com.maxim.multicamera.multiCamera.data.ShareCameraId

class MultiCameraViewModel(
    private val communication: MultiCameraCommunication,
    private val shareCameraId: ShareCameraId.Mutable,
    private val cameraManagerWrapper: CameraManagerWrapper,
    private val navigation: Navigation.Update,
    private val clearViewModel: ClearViewModel
) : ViewModel(), Communication.Observe<MultiCameraState>, Init, GoBack, SaveAndRestore {
    private var selection = Pair(-1, -1)

    init {
        Log.d("MyLog", "vm: $this")
    }

    override fun init(isFirstRun: Boolean) {
        if (isFirstRun) {
            communication.update(
                MultiCameraState.Base(
                    cameraManagerWrapper.physicalCameras(
                        shareCameraId.readLogical()
                    ), selection, false
                )
            )
        }
    }

    fun start() {
        val cameras = cameraManagerWrapper.physicalCameras(shareCameraId.readLogical())
        shareCameraId.savePhysical(Pair(cameras[selection.first], cameras[selection.second]))
        navigation.update(CameraScreen)
    }

    fun choose(pos: Int, index: Int) {
        selection = if (pos == 0) Pair(index, selection.second) else Pair(selection.first, index)
        communication.update(
            MultiCameraState.Base(
                cameraManagerWrapper.physicalCameras(shareCameraId.readLogical()),
                selection,
                selection.first != -1 && selection.second != -1 && selection.first != selection.second
            )
        )
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<MultiCameraState>) {
        communication.observe(owner, observer)
    }

    override fun goBack() {
        navigation.update(Screen.Pop)
        clearViewModel.clear(MultiCameraViewModel::class.java)
        Log.d("MyLog", "goBack")
    }

    override fun save(bundleWrapper: BundleWrapper.Save) {
        communication.save(KEY, bundleWrapper)
        bundleWrapper.save(SELECTION_KEY, selection)
    }

    override fun restore(bundleWrapper: BundleWrapper.Restore) {
        communication.restore(KEY, bundleWrapper)
        selection = bundleWrapper.restore(SELECTION_KEY)!!
    }

    companion object {
        private const val KEY = "multi_camera_viewmodel_restore_key"
        private const val SELECTION_KEY = "multi_camera_viewmodel_selection_restore_key"
    }
}