package com.maxim.multicamera.camera.presentation

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.maxim.multicamera.core.presentation.BaseFragment
import com.maxim.multicamera.databinding.FragmentCameraBinding

class CameraFragment : BaseFragment<CameraViewModel, FragmentCameraBinding>(), CameraController {
    override val viewModelClass = CameraViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCameraBinding.inflate(inflater, container, false)

    private lateinit var cameraManager: CameraManager
    private var backgroundThread: HandlerThread? = null
    private var handler: Handler? = null
    private var previewSession: CameraCaptureSession? = null

    private lateinit var camera: CameraService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                //previewSession?.stopRepeating()
                //previewSession = null
                viewModel.goBack()
            }
        }
        super.onViewCreated(view, savedInstanceState)

        cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager
    }

    override fun onResume() {
        super.onResume()
        if (backgroundThread == null)
            createBackgroundThread()
        camera = CameraService.Base(viewModel.logicalId(), cameraManager, this)
        if (handler == null)
            createBackgroundThread()
        camera.openCamera(handler!!)
    }

    override fun onPause() {
        super.onPause()
        camera.closeCamera()
        stopBackgroundThread()
    }

    private fun createBackgroundThread() {
        backgroundThread = HandlerThread("camera-background")
        backgroundThread!!.start()
        handler = Handler(backgroundThread!!.looper)
    }

    private fun stopBackgroundThread() {
        backgroundThread!!.quitSafely()
        backgroundThread!!.join()
        backgroundThread = null
        handler = null
    }

    private lateinit var sessionConfig: SessionConfiguration


    override fun startPhysicalCameras(cameraDevice: CameraDevice) {
        if (binding.textureViewOne.isAvailable && binding.textureViewTwo.isAvailable) {
            startPhysicalAfterTextureAvailable(cameraDevice)
        } else {
            val listener = object : SurfaceTextureListener {
                override fun onSurfaceTextureAvailable(
                    surface: SurfaceTexture,
                    width: Int,
                    height: Int
                ) {
                    if (binding.textureViewOne.isAvailable && binding.textureViewTwo.isAvailable)
                        startPhysicalAfterTextureAvailable(cameraDevice)
                }

                override fun onSurfaceTextureSizeChanged(
                    surface: SurfaceTexture,
                    width: Int,
                    height: Int
                ) = Unit

                override fun onSurfaceTextureDestroyed(surface: SurfaceTexture) = false
                override fun onSurfaceTextureUpdated(surface: SurfaceTexture) = Unit
            }
            if (!binding.textureViewOne.isAvailable) {
                binding.textureViewTwo.surfaceTextureListener = listener
            }
            if (!binding.textureViewTwo.isAvailable) {
                binding.textureViewTwo.surfaceTextureListener = listener
            }
        }
    }

    private fun startPhysicalAfterTextureAvailable(cameraDevice: CameraDevice) {
        val surfaceOne = Surface(binding.textureViewOne.surfaceTexture)
        val configOne = OutputConfiguration(surfaceOne)
        configOne.setPhysicalCameraId(viewModel.physicalsIds().first)

        val surfaceTwo = Surface(binding.textureViewTwo.surfaceTexture)
        val configTwo = OutputConfiguration(surfaceTwo)
        configTwo.setPhysicalCameraId(viewModel.physicalsIds().second)

        sessionConfig = SessionConfiguration(SessionConfiguration.SESSION_REGULAR,
            listOf(configOne, configTwo),
            requireActivity().mainExecutor,
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(session: CameraCaptureSession) {
                    val captureRequest =
                        session.device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

                    captureRequest.addTarget(surfaceOne)
                    captureRequest.addTarget(surfaceTwo)

                    previewSession = session
                    session.setRepeatingRequest(captureRequest.build(), null, null)
                }

                override fun onConfigureFailed(session: CameraCaptureSession) = Unit
            })

        cameraDevice.createCaptureSession(sessionConfig)
    }
}

interface CameraController {
    fun startPhysicalCameras(cameraDevice: CameraDevice)
}