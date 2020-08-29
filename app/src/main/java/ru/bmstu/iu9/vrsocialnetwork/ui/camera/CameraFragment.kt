package ru.bmstu.iu9.vrsocialnetwork.ui.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.bmstu.iu9.vrsocialnetwork.R
import ru.bmstu.iu9.vrsocialnetwork.data.SensorMap
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CameraFragment : Fragment(), SensorEventListener {
	private var mPreviewView: PreviewView? = null
	private var mCaptureView: CardView? = null
	private var mImageCapture: ImageCapture? = null
	private var mPreview: Preview? = null
	private var mCamera: Camera? = null
	private lateinit var mOutputDirectory: File
	private var mCount = 0
	private lateinit var mSensorManager: SensorManager
	private lateinit var mAccelerometer: Sensor
	private var mCurrentLens = CameraSelector.LENS_FACING_BACK
	private var mCurrentAccelerometerParam : FloatArray? = null
	private lateinit var mCurrentVelocity: FloatArray
	private var mCurrentAcceleration: FloatArray? = null
	private lateinit var mPreviousAccelerometerParam: FloatArray
	private lateinit var mSensorPositions : SensorMap
	private var mPreviousTime : Long = 0
	private var mTime : Long = 0
	private lateinit var mRadiusVec : FloatArray
	private var mAccelerationStarted = false

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = inflater.inflate(R.layout.camera_preview, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		mCurrentVelocity = floatArrayOf(0f, 0f, 0f)
		mCurrentAcceleration = floatArrayOf(0f, 0f, 0f)
		mRadiusVec = floatArrayOf(0f, 0f, 0f)

		mSensorPositions = SensorMap()

		mSensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

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

	override fun onPause() {
		super.onPause()
		mSensorManager.unregisterListener(this)
	}

	override fun onResume() {
		super.onResume()
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
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
				}

				override fun onError(exception: ImageCaptureException) {
					Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
				}
			}
		)
		mSensorPositions[mCount] = ArrayList()
		mCurrentAccelerometerParam?.forEach {
			print("$it ")
			mSensorPositions[mCount]?.add(it)
		}
		println()
	}

	private fun navigateToNext(path: String) {
		val direction = CameraFragmentDirections.actionCameraFragmentToPostPreviewFragment(
			filePath = path,
			sensorMap = mSensorPositions
		)
		findNavController().navigate(direction)
	}

	override fun onSensorChanged(event: SensorEvent?) {
		if (event?.values != null) {
			mCurrentAccelerometerParam = event.values
			mTime = System.currentTimeMillis()
			if (mAccelerationStarted) {
				mCurrentAcceleration = floatArrayOf(
					mCurrentAccelerometerParam!![0] - mPreviousAccelerometerParam[0],
					mCurrentAccelerometerParam!![1] - mPreviousAccelerometerParam[1],
					mCurrentAccelerometerParam!![2] - mPreviousAccelerometerParam[2],
				)
				val deltaTime = (mTime - mPreviousTime).toFloat() / 1000
				mCurrentVelocity = floatArrayOf(
					mCurrentVelocity[0] + deltaTime * mCurrentAcceleration!![0],
					mCurrentVelocity[1] + deltaTime * mCurrentAcceleration!![1],
					mCurrentVelocity[2] + deltaTime * mCurrentAcceleration!![2],
				)
				mRadiusVec = floatArrayOf(
					mRadiusVec[0] + deltaTime * mCurrentAcceleration!![0],
					mRadiusVec[1] + deltaTime * mCurrentAcceleration!![1],
					mRadiusVec[2] + deltaTime * mCurrentAcceleration!![2],
				)

//				println(deltaTime)
			}
			mAccelerationStarted = true
			mPreviousTime = mTime
			mPreviousAccelerometerParam = mCurrentAccelerometerParam!!.copyOf()
		}
	}

	override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

	}

	companion object {
		private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
		private const val REQUEST_CODE_PERMISSION = 1111
		const val TAG = "CAMERA PREVIEW"
		private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
	}
}