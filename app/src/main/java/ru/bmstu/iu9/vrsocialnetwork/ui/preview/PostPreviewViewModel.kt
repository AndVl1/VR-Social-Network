package ru.bmstu.iu9.vrsocialnetwork.ui.preview

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post
import ru.bmstu.iu9.vrsocialnetwork.data.repository.MainRepository

class PostPreviewViewModel @ViewModelInject constructor(
	private val mMainRepository: MainRepository,
	@Assisted private val savedStateHandle: SavedStateHandle
): ViewModel(){

	private val mAuth = FirebaseAuth.getInstance()

	fun savePost(path: String) { // TODO add firebaseAuth user
		viewModelScope.launch {
			mMainRepository.addPost(Post(0, mAuth.currentUser?.displayName ?: "", "", "test", path))
		}
	}
}