package ru.bmstu.iu9.vrsocialnetwork.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import ru.bmstu.iu9.vrsocialnetwork.data.model.User
import javax.inject.Inject


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository @Inject constructor(private val mMainRepository: MainRepository) {
	private val mAuth = FirebaseAuth.getInstance()

	fun logOut() {
		mAuth.signOut()
        FirebaseMessaging.getInstance().unsubscribeFromTopic(mAuth.currentUser!!.uid)
	}

	suspend fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential): User {
		var authenticatedUser = User()
		val task = mAuth.signInWithCredential(googleAuthCredential).await()
		if (task != null) {
			val isNewUser: Boolean =
				task.additionalUserInfo!!.isNewUser
			val firebaseUser: FirebaseUser? = mAuth.currentUser
			if (firebaseUser != null) {
				val uid = firebaseUser.uid
				val name = firebaseUser.displayName ?: "No name"
				val email = firebaseUser.email ?: "No email"
				val user = User(
                    uId = uid,
                    name = name,
                    email = email
                )
				Log.d(TAG, user.toString())
				mMainRepository.addClient(user)
				user.isNew = isNewUser
				authenticatedUser = user
			}
            FirebaseMessaging.getInstance().subscribeToTopic(mAuth.currentUser!!.uid)
		} else {
			Log.e(TAG, "Not successful")
		}

		return authenticatedUser
	}

	fun createNewUserIfNorExist(user: User): MutableLiveData<User> {
		val newUserLiveData = MutableLiveData<User>()
		user.isCreated = true
		newUserLiveData.value = user
		return newUserLiveData
	}

	companion object {
		private const val TAG = "LOGIN REP"
	}
}