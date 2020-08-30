package ru.bmstu.iu9.vrsocialnetwork.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import ru.bmstu.iu9.vrsocialnetwork.data.model.User
import javax.inject.Inject

class SplashRepository @Inject constructor() {
	val mAuth = FirebaseAuth.getInstance()
	private var mUser = User()

	fun checkAuthentication(): MutableLiveData<User> {
		val isUserAuthenticated = MutableLiveData<User>()
		val firebaseUser = mAuth.currentUser
		if (firebaseUser == null) {
			mUser.isAuthenticated = false
		} else {
			mUser = User(
				uId = firebaseUser.uid,
				isAuthenticated = true
			)
		}
		isUserAuthenticated.value = mUser
		return isUserAuthenticated
	}

}