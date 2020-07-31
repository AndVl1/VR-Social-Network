package ru.bmstu.iu9.vrsocialnetwork.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.bmstu.iu9.vrsocialnetwork.R

@AndroidEntryPoint
class SplashFragment: Fragment() {

	private val mViewModel by viewModels<SplashViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? = inflater.inflate(R.layout.fragment_splash, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		mViewModel.checkIfUserIsAuthenticated()
		mViewModel.isUserAuthenticatedLiveData.observe(viewLifecycleOwner, Observer {
			if (!it.isAuthenticated!!) {
				navigateToAuth()
			} else {
				navigateToHome()
			}
		})
	}

	private fun navigateToAuth() {
		val directions = SplashFragmentDirections.actionSplashScreenFragmentToLoginFragment()
		findNavController().navigate(directions)
	}

	private fun navigateToHome() {
		val directions = SplashFragmentDirections.actionSplashScreenFragmentToHomeFragment()
		findNavController().navigate(directions)
	}

}