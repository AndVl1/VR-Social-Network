package ru.bmstu.iu9.vrsocialnetwork.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.bmstu.iu9.vrsocialnetwork.R
import java.lang.Exception

class CameraFragment : Fragment() {
    var mRoot: View? = null
    private var mPreviewView : PreviewView? = null
    private var mCaptureView : ImageView? = null
    private var mPreview: Preview? = null
    private var mCamera: Camera? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mRoot == null)
            mRoot = inflater.inflate(R.layout.camera_preview, container, false)

        mPreviewView = mRoot?.findViewById(R.id.camera_captureView)
        mCaptureView = mRoot?.findViewById(R.id.camera_capture)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION)
        }

        mCaptureView?.setOnClickListener {
            takePhoto()
        }

        return mRoot
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(activity?.applicationContext!!, it) == PackageManager.PERMISSION_GRANTED
        // TODO replace activity.applicationContext
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(activity?.applicationContext, getString(R.string.camera_permissions_error), Toast.LENGTH_SHORT).show()
                // TODO return to previous fragment
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(activity?.applicationContext!!) // TODO replace

        cameraProviderFuture.addListener( Runnable {
            val cameraProvider : ProcessCameraProvider = cameraProviderFuture.get()

            mPreview = Preview.Builder().build()
            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

            try {
                cameraProvider.unbindAll()
                mCamera = cameraProvider.bindToLifecycle(this, cameraSelector, mPreview)
                mPreview?.setSurfaceProvider(mPreviewView?.createSurfaceProvider())
            } catch (e: Exception) {
                Log.e(TAG, e.localizedMessage!!)
            }
        }, ContextCompat.getMainExecutor(activity?.applicationContext))
    }

    private fun takePhoto() {
        // TODO
    }

    companion object{
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, "android.permission.WRITE_EXTERNAL_STORAGE")
        private const val REQUEST_CODE_PERMISSION = 1111
        const val TAG = "CAMERA PREVIEW"
    }
}