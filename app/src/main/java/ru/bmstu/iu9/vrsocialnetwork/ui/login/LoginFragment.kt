package ru.bmstu.iu9.vrsocialnetwork.ui.login

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

import ru.bmstu.iu9.vrsocialnetwork.R
import ru.bmstu.iu9.vrsocialnetwork.data.model.User

@AndroidEntryPoint
class LoginFragment : Fragment() {

	private val loginViewModel: LoginViewModel by viewModels()
	private lateinit var googleSignInClient: GoogleSignInClient

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_login, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val googleLoginButton = view.findViewById<ImageButton>(R.id.google_log)
		googleLoginButton.setOnClickListener {
			signIn()
		}

		initGoogleSignInClient()

		val loadingProgressBar = view.findViewById<ProgressBar>(R.id.loading)


	}

	private fun signIn() {
		val signInIntent = googleSignInClient.signInIntent
		startActivityForResult(signInIntent, RC_SIGN_IN)
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == RC_SIGN_IN) {
			Log.d(TAG, "on activity result")
			val task = GoogleSignIn.getSignedInAccountFromIntent(data)
			try {
				val googleSignInAccount = task.getResult(ApiException::class.java)
				if (googleSignInAccount != null) {
					getGoogleAuthCredential(googleSignInAccount)
				}
			}catch (e: ApiException) {
				Log.e(TAG, e.message)
			}
		}
	}

	private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount){
		val googleTokenId = googleSignInAccount.idToken
		val googleAuthCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
		Log.d(TAG, "getGoogleAuthCredential")
		signInWithGoogleCredential(googleAuthCredential)
	}

	private fun signInWithGoogleCredential(googleAuthCredential: AuthCredential) {
		loginViewModel.authWithGoogle(googleAuthCredential)
		loginViewModel.userLiveData.observe(viewLifecycleOwner, Observer {
			if (it.isNew == true) {
				createNewUser(it)
			} else {
				navigateToHome()
			}
		})
	}

	private fun createNewUser(user: User) {
		loginViewModel.createNewUser(user)
		loginViewModel.createdUserLiveData.observe(this, Observer {
			navigateToHome()
		})
	}

	private fun navigateToHome() {
		val directions = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
		findNavController().navigate(directions)
	}

	private fun initGoogleSignInClient() {
		val googleSignInOptions = GoogleSignInOptions
			.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(getString(R.string.default_web_client_id))
			.requestEmail()
			.build()

		googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
	}

	companion object {
		private const val RC_SIGN_IN = 101
		private const val TAG = "LOGIN FRAGMENT"
	}
}