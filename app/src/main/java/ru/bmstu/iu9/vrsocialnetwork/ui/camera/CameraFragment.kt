package ru.bmstu.iu9.vrsocialnetwork.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.bmstu.iu9.vrsocialnetwork.R
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {
	var mRoot: View? = null
	private var mPreviewView : PreviewView? = null
	private var mCaptureView : CardView? = null
	private var mImageCapture: ImageCapture? = null
//	private var mVideoCapture: VideoCapture? = null
	private var mImageAnalyzer: ImageAnalysis? = null
	private var mPreview: Preview? = null
	private var mCamera: Camera? = null
	private lateinit var mOutputDirectory: File

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		if (mRoot == null)
			mRoot = inflater.inflate(R.layout.camera_preview, container, false)

		mPreviewView = mRoot?.findViewById(R.id.camera_captureView)
		mCaptureView = mRoot?.findViewById(R.id.camera_capture)

		mOutputDirectory = getOutputDirectory()

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
		ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
		if (requestCode == REQUEST_CODE_PERMISSION) {
			if (allPermissionsGranted()) {
				startCamera()
			} else {
				Toast.makeText(requireContext(), getString(R.string.camera_permissions_error), Toast.LENGTH_SHORT).show()
			}
		}
	}

	private fun startCamera() {
		val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext()) // TODO replace

		cameraProviderFuture.addListener( Runnable {
			val cameraProvider : ProcessCameraProvider = cameraProviderFuture.get()

			mPreview = Preview.Builder().build()

			mImageCapture = ImageCapture.Builder()
				.build()
//			mVideoCapture = VideoCapture.Builder().apply {
//
//			}.build()

			val cameraSelector = CameraSelector.Builder()
				.requireLensFacing(CameraSelector.LENS_FACING_FRONT)
				.build()

			try {
				cameraProvider.unbindAll()
				mCamera = cameraProvider.bindToLifecycle(this, cameraSelector, mPreview, mImageCapture)
				mPreview?.setSurfaceProvider(mPreviewView?.createSurfaceProvider())
			} catch (e: Exception) {
				Log.e(TAG, e.localizedMessage!!)
			}
		}, ContextCompat.getMainExecutor(requireContext()))
	}

	private fun getOutputDirectory(): File {
		val mediaDir = requireContext().externalMediaDirs
			.firstOrNull()?.let {
				File(it, resources.getString(R.string.app_name)).apply {
					mkdirs()
				}
			}
		return mediaDir!!
	}

//	private fun captureVideo() {
//		val videoCapture = mVideoCapture ?: return
//	}

	private fun takePhoto() {
		val imageCapture = mImageCapture ?: return

		val photoFile = File(
			mOutputDirectory,
			SimpleDateFormat(FILENAME_FORMAT, Locale.US)
				.format(System.currentTimeMillis()) + ".jpg"
		)
		val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

		imageCapture.takePicture(
			outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
				override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
					val savedUri = Uri.fromFile(photoFile)
					val msg = "Photo at $savedUri"
					Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
					if (savedUri.path != null) {
						navigateToNext(savedUri?.path ?: "")
					} else {
						Toast.makeText(requireContext(), "Image not saved", Toast.LENGTH_SHORT).show()
						Log.e(TAG, "Image npt saved")
					}
				}

				override fun onError(exception: ImageCaptureException) {
					Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
				}

			}
		)
	}

	private fun navigateToNext(path: String) {
		val direction = CameraFragmentDirections.actionCameraFragmentToPostPreviewFragment(
			filePath = path
		)
		findNavController().navigate(direction)
	}

	companion object{
		private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
		private const val REQUEST_CODE_PERMISSION = 1111
		const val TAG = "CAMERA PREVIEW"
		private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
		private const val BURST_FRAMERATE = 10
	}
}