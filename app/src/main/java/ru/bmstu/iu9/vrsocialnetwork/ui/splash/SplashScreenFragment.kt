package ru.bmstu.iu9.vrsocialnetwork.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.bmstu.iu9.vrsocialnetwork.R

class SplashScreenFragment: Fragment() {
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		val root = inflater.inflate(R.layout.fragment_splash, container, false)
		// todo
		return root
	}
}