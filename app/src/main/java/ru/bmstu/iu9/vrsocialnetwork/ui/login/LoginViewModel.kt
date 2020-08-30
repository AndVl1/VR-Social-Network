package ru.bmstu.iu9.vrsocialnetwork.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import ru.bmstu.iu9.vrsocialnetwork.data.repository.LoginRepository

import ru.bmstu.iu9.vrsocialnetwork.data.model.User

class LoginViewModel @ViewModelInject constructor (private val loginRepository: LoginRepository)
	: ViewModel() {

	lateinit var userLiveData :LiveData<User>
	lateinit var createdUserLiveData: LiveData<User>

	fun authWithGoogle(googleAuthCredential: AuthCredential) {
//		userLiveData = loginRepository.firebaseSignInWithGoogle(googleAuthCredential)
		userLiveData = liveData(viewModelScope.coroutineContext) {
			emit(loginRepository.firebaseSignInWithGoogle(googleAuthCredential))
		}
	}

	fun createNewUser(user: User) {
		createdUserLiveData = loginRepository.createNewUserIfNorExist(user)
	}
}