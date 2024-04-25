package com.maxim.multicamera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.util.Log
import android.view.Surface
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.maxim.multicamera.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val myCameras = mutableListOf<CameraService>()
    private var cameraManager: CameraManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            myCameras.clear()
            myCameras.addAll(cameraManager!!.cameraIdList.map {
                CameraService(it, cameraManager!!)
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }

        myCameras.forEach {
            it.open()
        }
        //myCameras[0].open()
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

    inner class CameraService(private val cameraId: String, private val cameraManager: CameraManager) {
        private var cameraDevice: CameraDevice? = null
        private var captureSession: CameraCaptureSession? = null
        private val cameraCallback = object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                cameraDevice = camera
                createCameraPreviewSession(cameraId)
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
                cameraManager.openCamera(cameraId, cameraCallback, null)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        fun close() {
            cameraDevice?.let {
                it.close()
                cameraDevice = null
            }
        }

        fun isOpen() = cameraDevice != null

        private fun createCameraPreviewSession(cameraId: String) {
            val texture = if (cameraId == "0") binding.textureViewOne.surfaceTexture else binding.textureViewTwo.surfaceTexture
            val surface = Surface(texture)

            try {
                val builder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                builder.addTarget(surface)
                cameraDevice!!.createCaptureSession(listOf(surface), object : CameraCaptureSession.StateCallback() {
                    override fun onConfigured(session: CameraCaptureSession) {
                        captureSession = session
                        try {
                            captureSession!!.setRepeatingRequest(builder.build(), null, null)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onConfigureFailed(session: CameraCaptureSession) = Unit
                }, null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}