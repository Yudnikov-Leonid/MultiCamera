package com.maxim.multicamera.camera.presentation

import android.content.Context
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureFailure
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.TotalCaptureResult
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import android.view.ViewGroup
import com.maxim.multicamera.core.presentation.BaseFragment
import com.maxim.multicamera.databinding.FragmentCameraBinding

class CameraFragment : BaseFragment<CameraViewModel, FragmentCameraBinding>(), CameraController {
    override val viewModelClass = CameraViewModel::class.java
    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCameraBinding.inflate(inflater, container, false)

    private lateinit var cameraManager: CameraManager
    private var backgroundThread: HandlerThread? = null
    private var handler: Handler? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraManager = requireContext().getSystemService(Context.CAMERA_SERVICE) as CameraManager

        viewModel.observe(this) {
            it.show(this)
        }

        viewModel.init(savedInstanceState == null)
    }

    override fun onResume() {
        super.onResume()
        if (backgroundThread == null)
            createBackgroundThread()
    }

    override fun onPause() {
        super.onPause()
        backgroundThread!!.quitSafely()
        backgroundThread!!.join()
        backgroundThread = null
        handler = null
    }

    override fun openLogical(id: String) {
        val camera = CameraService.Base(id, cameraManager, this)
        if (handler == null)
            createBackgroundThread()
        camera.openCamera(handler!!)
    }

    private fun createBackgroundThread() {
        backgroundThread = HandlerThread("camera-background")
        backgroundThread!!.start()
        handler = Handler(backgroundThread!!.looper)
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
                    Log.d("MyLog", "onConfigured")
                    val captureRequest =
                        session.device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

                    captureRequest.addTarget(surfaceOne)
                    captureRequest.addTarget(surfaceTwo)

                    session.capture(
                        captureRequest.build(),
                        object : CameraCaptureSession.CaptureCallback() {
                            override fun onCaptureCompleted(
                                session: CameraCaptureSession,
                                request: CaptureRequest,
                                result: TotalCaptureResult
                            ) {
                                super.onCaptureCompleted(session, request, result)
                                Log.d("MyLog", "onCaptureCompleted")
                            }

                            override fun onCaptureFailed(
                                session: CameraCaptureSession,
                                request: CaptureRequest,
                                failure: CaptureFailure
                            ) {
                                super.onCaptureFailed(session, request, failure)
                                Log.d("MyLog", "onCaptureFailed: ${failure.reason}")
                            }
                        },
                        null
                    )
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                    Log.d("MyLog", "onConfigureFailed")
                }
            })

        cameraDevice.createCaptureSession(sessionConfig)
    }
}

interface CameraController {
    fun openLogical(id: String)
    fun startPhysicalCameras(cameraDevice: CameraDevice)
}