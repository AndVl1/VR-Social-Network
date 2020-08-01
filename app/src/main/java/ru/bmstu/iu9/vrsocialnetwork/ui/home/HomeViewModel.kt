package ru.bmstu.iu9.vrsocialnetwork.ui.home

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import ru.bmstu.iu9.vrsocialnetwork.data.PostsDataSource
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post
import ru.bmstu.iu9.vrsocialnetwork.data.repository.MainRepository

class HomeViewModel @ViewModelInject constructor(
	mainRepository: MainRepository
) : ViewModel() {
	var postsLiveData: LiveData<PagedList<Post>>
	val dataSource = PostsDataSource(mainRepository, viewModelScope)

	init {
		val config = PagedList.Config.Builder()
			.setPageSize(30)
			.build()
//		postsLiveData = initializedPagedListBuilder(config).build()
		postsLiveData = LivePagedListBuilder(mainRepository.getLoadedPosts(viewModelScope), 20).build()
		Log.d(TAG, postsLiveData.value?.size?.toString() ?: "none")
	}

	fun getPosts(): LiveData<PagedList<Post>> = postsLiveData

	private fun initializedPagedListBuilder(config: PagedList.Config): LivePagedListBuilder<String, Post> {
		val dataSourceFactory = object : DataSource.Factory<String, Post>() {
			override fun create(): DataSource<String, Post> {
				return dataSource
			}
		}
		return LivePagedListBuilder<String, Post>(dataSourceFactory, config)
	}
//	val mPosts = LivePagedListBuilder(mainRepository.getPostsAsDataSource(), 20).build()

	companion object {
		private const val TAG = "HOME VM"
	}
}