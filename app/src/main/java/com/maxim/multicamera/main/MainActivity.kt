package com.maxim.multicamera.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCaptureSession.CaptureCallback
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CaptureFailure
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.TotalCaptureResult
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.View
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.maxim.multicamera.R
import com.maxim.multicamera.core.App
import com.maxim.multicamera.core.sl.ProvideViewModel
import com.maxim.multicamera.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), ProvideViewModel {
    private lateinit var binding: ActivityMainBinding

    private val myCameras = mutableListOf<CameraService>()
    private var cameraManager: CameraManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = viewModel(MainViewModel::class.java)
        viewModel.observe(this) {
            it.show(supportFragmentManager, R.id.container)
        }

        viewModel.init(savedInstanceState == null)

//        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
//        try {
//            myCameras.clear()
//            myCameras.addAll(cameraManager!!.cameraIdList.map {
//                CameraService(it, cameraManager!!)
//            })
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        myCameras.forEach { camera ->
//            val button = Button(this).apply {
//                val isFront = cameraManager!!.getCameraCharacteristics(camera.cameraId)
//                    .get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
//                val t = "${camera.cameraId} (${if (isFront) "FRONT" else "BACK"})"
//                text = t
//            }
//            binding.buttonsLayout.addView(button)
//            button.setOnClickListener {
//                camera.open()
//            }
//        }
    }

    override fun onResume() {
        super.onResume()

        val permissionList = mutableListOf<String>()
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.CAMERA)
        }
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissionList.isNotEmpty()) {
            requestPermissions(
                permissionList.toTypedArray(), 1
            )
        }
    }

    inner class CameraService(
        val cameraId: String,
        private val cameraManager: CameraManager
    ) {
        private var cameraDevice: CameraDevice? = null
        private var captureSession: CameraCaptureSession? = null

        private var physicalCameras = Pair("", "")

        private val cameraCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                //createCameraPreviewSession(cameraId)
                //startPhysicalCameras(cameraManager.getCameraCharacteristics(cameraId).physicalCameraIds.toList())
                binding.buttonsLayout.addView(Button(this@MainActivity).apply {
                    text = "Start"
                    setOnClickListener {
                        startPhysicalCameras(listOf(physicalCameras.first, physicalCameras.second))
                        binding.buttonsLayout.removeAllViews()
                    }
                })
                binding.buttonsLayout.visibility = View.GONE
                cameraManager.getCameraCharacteristics(cameraId).physicalCameraIds.forEach {
                    val buttonOne = RadioButton(this@MainActivity).apply {
                        text = it
                        setOnClickListener { _ ->
                            physicalCameras = Pair(it, physicalCameras.second)
                            if (physicalCameras.first.isNotEmpty() && physicalCameras.second.isNotEmpty() && physicalCameras.first != physicalCameras.second)
                                binding.buttonsLayout.visibility = View.VISIBLE
                            else binding.buttonsLayout.visibility = View.GONE
                        }
                    }
                    val buttonTwo = RadioButton(this@MainActivity).apply {
                        text = it
                        id = it.toInt()
                        setOnClickListener { _ ->
                            physicalCameras = Pair(physicalCameras.first, it)
                            if (physicalCameras.first.isNotEmpty() && physicalCameras.second.isNotEmpty() && physicalCameras.first != physicalCameras.second)
                                binding.buttonsLayout.visibility = View.VISIBLE
                            else binding.buttonsLayout.visibility = View.GONE
                        }
                    }
                    binding.physicalButtonGroupOne.addView(buttonOne)
                    binding.physicalButtonGroupTwo.addView(buttonTwo)
                }
                Log.d("MyLog", "opened: $cameraId")
            }

            override fun onDisconnected(camera: CameraDevice) {
                cameraDevice!!.close()
                cameraDevice = null
            }

            override fun onError(camera: CameraDevice, error: Int) {
                Log.d("MyLog", "error: $error, cameraId: ${camera.id}")
            }
        }

        @SuppressLint("MissingPermission")
        fun open() {
            try {
                val physicalCameras =
                    cameraManager.getCameraCharacteristics(cameraId).physicalCameraIds
                if (physicalCameras.isEmpty()) {
                    Toast.makeText(this@MainActivity, "No physical cameras", Toast.LENGTH_LONG)
                        .show()
                } else {
                    cameraManager.openCamera(cameraId, cameraCallback, null)
                    binding.buttonsLayout.removeAllViews()
                }
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }


        private lateinit var sessionConfig: SessionConfiguration
        fun startPhysicalCameras(ids: List<String>) {
            val surfaceOne = Surface(binding.textureViewOne.surfaceTexture)
            val configOne = OutputConfiguration(surfaceOne)
            configOne.setPhysicalCameraId(ids[0])

            val surfaceTwo = Surface(binding.textureViewTwo.surfaceTexture)
            val configTwo = OutputConfiguration(surfaceTwo)
            configTwo.setPhysicalCameraId(ids[1])

            sessionConfig = SessionConfiguration(SessionConfiguration.SESSION_REGULAR,
                listOf(configOne, configTwo),
                mainExecutor,
                object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        Log.d("MyLog", "onConfigured")
                        val captureRequest =
                            session.device.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)

                        captureRequest.addTarget(surfaceOne)
                        captureRequest.addTarget(surfaceTwo)

                        session.capture(captureRequest.build(), object : CaptureCallback() {
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
                        }, null)
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) {
                        Log.d("MyLog", "onConfigureFailed")
                    }
                })

            cameraDevice!!.createCaptureSession(sessionConfig)
        }

        fun close() {
            cameraDevice?.let {
                it.close()
                cameraDevice = null
            }
        }

        fun isOpen() = cameraDevice != null

        private fun createCameraPreviewSession(cameraId: String) {
            val texture =
                if (cameraId == "0") binding.textureViewOne.surfaceTexture else binding.textureViewTwo.surfaceTexture
            val surface = Surface(texture)

            try {
                val builder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                builder.addTarget(surface)
                cameraDevice!!.createCaptureSession(
                    listOf(surface),
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigured(session: CameraCaptureSession) {
                            captureSession = session
                            try {
                                captureSession!!.setRepeatingRequest(builder.build(), null, null)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }

                        override fun onConfigureFailed(session: CameraCaptureSession) = Unit
                    },
                    null
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun <T : ViewModel> viewModel(clasz: Class<T>): T {
        return (application as App).viewModel(clasz)
    }
}