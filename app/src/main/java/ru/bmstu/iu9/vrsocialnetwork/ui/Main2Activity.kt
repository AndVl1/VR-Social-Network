package ru.bmstu.iu9.vrsocialnetwork.ui

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.bottomappbar.BottomAppBar
import dagger.hilt.android.AndroidEntryPoint
import ru.bmstu.iu9.vrsocialnetwork.R
import ru.bmstu.iu9.vrsocialnetwork.databinding.BottomTestBinding

@AndroidEntryPoint
class Main2Activity: AppCompatActivity() {

	lateinit var navController: NavController
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
//		navController = findNavController(R.id.main_graph)
		setupBottomNavigationAndFab()
//		navView.setupWithNavController(navController)
	}

	private fun setupBottomNavigationAndFab() {
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
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		val inflater = menuInflater
		inflater.inflate(R.menu.bottom_app_bar, menu)
		return true
	}
}