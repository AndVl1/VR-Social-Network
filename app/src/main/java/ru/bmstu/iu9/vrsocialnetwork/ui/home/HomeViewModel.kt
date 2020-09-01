package ru.bmstu.iu9.vrsocialnetwork.ui.home

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.Dispatchers
import ru.bmstu.iu9.vrsocialnetwork.data.PostsDataSource
import ru.bmstu.iu9.vrsocialnetwork.utils.Resource
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post
import ru.bmstu.iu9.vrsocialnetwork.data.repository.MainRepository

class HomeViewModel @ViewModelInject constructor(
	private val mainRepository: MainRepository
) : ViewModel() {

	fun downloadPosts() = liveData(Dispatchers.IO) {
		emit(Resource.loading(data = null))
		try {
			emit(Resource.success(data = mainRepository.getPosts()))
		} catch (e: Exception) {
			emit(Resource.error(data = null, msg = e.message ?: "Error Occurred!"))
		}
	}

	companion object {
		private const val TAG = "HOME VM"
	}
}