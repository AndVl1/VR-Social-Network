package ru.bmstu.iu9.vrsocialnetwork.ui.splash

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.bmstu.iu9.vrsocialnetwork.data.model.User
import ru.bmstu.iu9.vrsocialnetwork.data.repository.SplashRepository

class SplashViewModel @ViewModelInject constructor(private val mRepo: SplashRepository) : ViewModel() {

	lateinit var userLiveData: LiveData<User>
	lateinit var isUserAuthenticatedLiveData: LiveData<User>

	fun checkIfUserIsAuthenticated() {
		isUserAuthenticatedLiveData = mRepo.checkAuthentication()
	}
}