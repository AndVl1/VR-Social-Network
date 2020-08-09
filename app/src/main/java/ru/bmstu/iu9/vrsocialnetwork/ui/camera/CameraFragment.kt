package ru.bmstu.iu9.vrsocialnetwork.ui.camera

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.impl.VideoCaptureConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemServiceName
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.bmstu.iu9.vrsocialnetwork.R
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {
	private var mPreviewView: PreviewView? = null
	private var mCaptureView: CardView? = null
	private var mImageCapture: ImageCapture? = null
	private var mPreview: Preview? = null
	private var mCamera: Camera? = null
	private lateinit var mOutputDirectory: File
	private var mCount = 0
	private lateinit var mSensorManager: SensorManager
	private var mCurrentLens = CameraSelector.LENS_FACING_BACK

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = inflater.inflate(R.layout.camera_preview, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		mSensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager

		mPreviewView = view.findViewById(R.id.camera_captureView)
		mPreviewView?.setOnClickListener {
			/** Changing camera on click to preview */
			mCurrentLens = if (mCurrentLens == CameraSelector.LENS_FACING_BACK) {
				CameraSelector.LENS_FACING_FRONT
			} else {
				CameraSelector.LENS_FACING_BACK
			}
			startCamera(mCurrentLens)
		}
		mCaptureView = view.findViewById(R.id.camera_capture)

		mOutputDirectory = getOutputDirectory()

		if (allPermissionsGranted()) {
			startCamera(CameraSelector.LENS_FACING_BACK)
		} else {
			requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSION)
		}
		val stopButton: Button = view.findViewById(R.id.camera_stop)

		mCaptureView?.setOnClickListener {
			takePhoto()
			mCount++
			if (mCount >= 10) {
				stopButton.visibility = View.VISIBLE
			}
		}

		stopButton.setOnClickListener {
			navigateToNext(Uri.fromFile(mOutputDirectory).path.toString())
		}
	}

	private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
		ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
	}

	override fun onRequestPermissionsResult(
		requestCode: Int,
		permissions: Array<out String>,
		grantResults: IntArray
	) {
		if (requestCode == REQUEST_CODE_PERMISSION) {
			if (allPermissionsGranted()) {
				startCamera(mCurrentLens)
			} else {
				Toast.makeText(
					requireContext(),
					getString(R.string.camera_permissions_error),
					Toast.LENGTH_SHORT
				).show()
			}
		}
	}

	private fun startCamera(lens: Int) {
		val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

		cameraProviderFuture.addListener(Runnable {
			val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

			mPreview = Preview.Builder().build()

			mImageCapture = ImageCapture.Builder()
				.build()

			val cameraSelector = CameraSelector.Builder()
				.requireLensFacing(lens)
				.build()

			try {
				cameraProvider.unbindAll()
				mCamera =
					cameraProvider.bindToLifecycle(this, cameraSelector, mPreview, mImageCapture)
				mPreview?.setSurfaceProvider(mPreviewView?.createSurfaceProvider())
			} catch (e: Exception) {
				Log.e(TAG, e.localizedMessage!!)
			}
		}, ContextCompat.getMainExecutor(requireContext()))
	}

	private fun getOutputDirectory(): File {
		val dirName = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
			.format(System.currentTimeMillis())

		val mediaDir = requireContext().externalMediaDirs
			.firstOrNull()?.let {
				File(it, "${resources.getString(R.string.app_name)}/${dirName}").apply {
					mkdirs()
				}
			}
		return mediaDir!!
	}

	private fun takePhoto() {
		val imageCapture = mImageCapture ?: return

		val photoFile = File(
			mOutputDirectory,
			"${mCount}.jpg"
		)
		val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

		imageCapture.takePicture(
			outputOptions,
			ContextCompat.getMainExecutor(requireContext()),
			object : ImageCapture.OnImageSavedCallback {
				override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
					val savedUri = Uri.fromFile(photoFile)
					if (savedUri.path != null) {
						Log.d(TAG, savedUri?.path ?: "")
					} else {
						Toast.makeText(requireContext(), "Image not saved", Toast.LENGTH_SHORT)
							.show()
						Log.e(TAG, "Image npt saved")
					}

					// TODO take sensors pos
				}

				override fun onError(exception: ImageCaptureException) {
					Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
				}
			}
		)
		val rotationMatrix = FloatArray(9)
		val orientationAngles = FloatArray(3)
		SensorManager.getOrientation(rotationMatrix, orientationAngles)
		Log.d(TAG, "${rotationMatrix}, $orientationAngles")
		rotationMatrix.forEach {
			println(it)
		}
		orientationAngles.forEach {
			println(it)
		}
	}

	private fun navigateToNext(path: String) {
		val direction = CameraFragmentDirections.actionCameraFragmentToPostPreviewFragment(
			filePath = path
		)
		findNavController().navigate(direction)
	}

	companion object {
		private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
		private const val REQUEST_CODE_PERMISSION = 1111
		const val TAG = "CAMERA PREVIEW"
		private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
		private const val BURST_FRAMERATE = 10
	}
}