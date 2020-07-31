package ru.bmstu.iu9.vrsocialnetwork.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.bmstu.iu9.vrsocialnetwork.data.model.User
import javax.inject.Inject


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository @Inject constructor() {
    private val mAuth = FirebaseAuth.getInstance()

    fun logOut() {
        mAuth.signOut()
    }

    fun firebaseSignInWithGoogle(googleAuthCredential: AuthCredential): MutableLiveData<User> {
        val authenticatedUserMutableLiveData =
            MutableLiveData<User>()
        // TODO run in coroutine
        mAuth.signInWithCredential(googleAuthCredential).addOnCompleteListener { authTask ->
            if (authTask.isSuccessful) {
                val isNewUser: Boolean =
                    authTask.result!!.additionalUserInfo!!.isNewUser
                val firebaseUser: FirebaseUser? = mAuth.currentUser
                if (firebaseUser != null) {
                    val uid = firebaseUser.uid
                    val name = firebaseUser.displayName?: "No name"
                    val email = firebaseUser.email?: "No email"
                    val user = User(
                            uId = uid,
                            name = name,
                            email = email
                        )
                    user.isNew = isNewUser
                    authenticatedUserMutableLiveData.value = user
                }
            } else {
                Log.e(TAG, authTask.exception?.message ?: "Not successful")
            }
        }
        return authenticatedUserMutableLiveData
    }

    fun createNewUserIfNorExist(user: User): MutableLiveData<User> {
        val newUserLiveData = MutableLiveData<User>()
        // TODO add to firebase (or send to server so it will create)
        user.isCreated = true
        newUserLiveData.value = user
        return newUserLiveData
    }

    companion object {
        private const val TAG = "LOGIN REP"
    }
}