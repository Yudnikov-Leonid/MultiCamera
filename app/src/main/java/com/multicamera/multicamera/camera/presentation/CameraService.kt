package com.multicamera.multicamera.camera.presentation

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Handler
import android.util.Size
import com.multicamera.multicamera.camera.data.ComparableByArea
import java.util.Collections

interface CameraService {
    fun openCamera(handler: Handler)
    fun closeCamera()
    fun isOpen(): Boolean
    fun getOptimalPreviewSize(
        textureViewWidth: Int,
        textureViewHeight: Int,
        maxWidth: Int,
        maxHeight: Int,
        aspectRatio: Size,
    ): Size

    fun getCaptureSize(comparator: Comparator<Size>): Size
    fun cameraId(): String

    class Base(
        private val cameraId: String,
        private val cameraManager: CameraManager,
        private val manageCamera: CameraController
    ) : CameraService {
        private var cameraDevice: CameraDevice? = null
        private val cameraCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                manageCamera.startPhysicalCameras(cameraDevice!!)
            }

            override fun onDisconnected(camera: CameraDevice) {
                cameraDevice!!.close()
                cameraDevice = null
            }

            override fun onError(camera: CameraDevice, error: Int) = Unit
        }

        @SuppressLint("MissingPermission")
        override fun openCamera(handler: Handler) {
            cameraManager.openCamera(cameraId, cameraCallback, handler)
        }

        override fun closeCamera() {
            cameraDevice?.let {
                it.close()
                cameraDevice = null
            }
        }

        override fun isOpen() = cameraDevice != null
        override fun getOptimalPreviewSize(
            textureViewWidth: Int,
            textureViewHeight: Int,
            maxWidth: Int,
            maxHeight: Int,
            aspectRatio: Size,
        ): Size {
            val map = cameraManager.getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) ?: return Size(0, 0)
            val choices = map.getOutputSizes(SurfaceTexture::class.java)

            val bigEnough = ArrayList<Size>()
            val notBigEnough = ArrayList<Size>()

            for (option in choices) {
                if (option.height == option.width * aspectRatio.height / aspectRatio.width) {
                    if (option.width >= textureViewWidth && option.height >= textureViewHeight)
                        bigEnough.add(option)
                    else
                        notBigEnough.add(option)
                }
            }

            val size =  when {
                notBigEnough.size > 0 -> Collections.min(notBigEnough, ComparableByArea())
                bigEnough.size > 0 -> Collections.min(bigEnough, ComparableByArea())
                else -> choices[0]
            }
            return size
        }

        override fun getCaptureSize(comparator: Comparator<Size>): Size {
            val map = cameraManager.getCameraCharacteristics(cameraId)
                .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP) ?: return Size(0, 0)
            return map.getOutputSizes(ImageFormat.JPEG).asList().maxWith(comparator) ?: Size(0, 0)
        }

        override fun cameraId() = cameraId
    }
}