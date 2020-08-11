package ru.bmstu.iu9.vrsocialnetwork.ui.preview

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post
import ru.bmstu.iu9.vrsocialnetwork.data.repository.MainRepository

class PostPreviewViewModel @ViewModelInject constructor(
	private val mMainRepository: MainRepository,
	@Assisted private val savedStateHandle: SavedStateHandle
): ViewModel(){

	private val mAuth = FirebaseAuth.getInstance()
	lateinit var mCompleteLiveData : LiveData<Boolean>

	fun savePost(path: String) {
		mCompleteLiveData = liveData(viewModelScope.coroutineContext) {
			emit(mMainRepository.addPost(
				Post(
					id = 0,
					authorID = mAuth.currentUser?.uid ?: "",
					authorName = mAuth.currentUser?.displayName ?: "",
					text = "",
					imageLink = path
				),
				viewModelScope
			))
		}
	}

	companion object {
		private const val TAG = "PREVIEW VM"
	}
}