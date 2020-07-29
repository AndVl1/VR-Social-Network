package ru.bmstu.iu9.vrsocialnetwork.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.bottomappbar.BottomAppBar
import dagger.hilt.android.AndroidEntryPoint
import ru.bmstu.iu9.vrsocialnetwork.R
import ru.bmstu.iu9.vrsocialnetwork.databinding.BottomTestBinding
import ru.bmstu.iu9.vrsocialnetwork.ui.camera.CameraFragmentDirections
import ru.bmstu.iu9.vrsocialnetwork.ui.home.HomeFragmentDirections

@AndroidEntryPoint
class Main2Activity: AppCompatActivity(), NavController.OnDestinationChangedListener {

	lateinit var mAppBar: BottomAppBar
	//private val mBinding:
	private lateinit var mMainBinding : BottomTestBinding

	val mCurrentNavigationFragment: Fragment?
		get() = supportFragmentManager.findFragmentById(R.id.nav_host_fragment2)
			?.childFragmentManager
			?.fragments
			?.first()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mMainBinding = BottomTestBinding.inflate(layoutInflater)
		setContentView(mMainBinding.root)

//		val navView: NavigationView = findViewById(R.id.navigation_view)
//
		setupBottomNavigationAndFab()
//		navView.setupWithNavController(navController)
	}

	private fun setupBottomNavigationAndFab() {
		findNavController(R.id.nav_host_fragment2)
			.addOnDestinationChangedListener(this)

		mAppBar = findViewById(R.id.bottom_app_bar)
		setSupportActionBar(mAppBar)
		mAppBar.setNavigationOnClickListener { v ->
//			TODO("Bottom sheet with menu")
		}
		mAppBar.setOnMenuItemClickListener {menuItem ->
			when (menuItem.itemId) {

				else -> false
			}
		}
		mMainBinding.fab.setOnClickListener {
			navigateToCamera()
		}
	}

	private fun navigateToHome() {
		setupBottomABForHome()
		val directions = HomeFragmentDirections.actionGlobalCameraFragment()
		findNavController(R.id.nav_host_fragment2).navigate(directions)
	}

	private fun navigateToCamera() {
		setupBottomABForCamera()
		val directions = CameraFragmentDirections.actionGlobalCameraFragment()
		findNavController(R.id.nav_host_fragment2).navigate(directions)
	}

	private fun setupBottomABForHome() {
		showAppBar()
		mMainBinding.fab.show()
	}

	private fun setupBottomABForCamera() {
		mMainBinding.fab.hide()
		hideBottomAppBar()
	}

	private fun showAppBar() {
		mMainBinding.run {
			bottomAppBar.performShow()
			bottomAppBar.animate().setListener(object : AnimatorListenerAdapter() {
				var isCanceled = false
				override fun onAnimationEnd(animation: Animator?) {
					if (isCanceled) return

					bottomAppBar.visibility = View.VISIBLE
					fab.visibility = View.VISIBLE
				}
				override fun onAnimationCancel(animation: Animator?) {
					isCanceled = true
				}
			})
		}
	}

	private fun hideBottomAppBar() {
		mMainBinding.run {
			
			bottomAppBar.performHide()
			bottomAppBar.animate().setListener(object : AnimatorListenerAdapter() {
				var isCanceled = false
				override fun onAnimationEnd(animation: Animator?) {
					if (isCanceled) return

					bottomAppBar.visibility = View.GONE
					fab.visibility = View.INVISIBLE
				}
				override fun onAnimationCancel(animation: Animator?) {
					isCanceled = true
				}
			})
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		val inflater = menuInflater
		inflater.inflate(R.menu.bottom_app_bar, menu)
		return true
	}

	override fun onDestinationChanged(
		controller: NavController,
		destination: NavDestination,
		arguments: Bundle?
	) {
		Log.d(TAG, "destination changed")

		when (destination.id) {
			R.id.homeFragment -> {
				Log.d(TAG, "homeFragment")
				setupBottomABForHome()
			}
			R.id.cameraFragment -> {
				Log.d(TAG, "cameraFragment")
				setupBottomABForCamera()
			}
		}
	}

	companion object {
		const val TAG = "MAIN"
	}
}