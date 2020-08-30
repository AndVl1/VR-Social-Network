package ru.bmstu.iu9.vrsocialnetwork.data

import android.util.Log
import androidx.paging.PageKeyedDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post
import ru.bmstu.iu9.vrsocialnetwork.data.repository.MainRepository

class PostsDataSource constructor(private val mRepository: MainRepository, private val mScope: CoroutineScope)
	: PageKeyedDataSource<String, Post>() {
	private val mJob = Job()
	private val mIOScope = CoroutineScope(Dispatchers.IO + mJob)

	override fun loadInitial(
		params: LoadInitialParams<String>,
		callback: LoadInitialCallback<String, Post>
	) {
		mScope.launch {
			try {
				val response = mRepository.fetchPosts()
				Log.d(TAG, response.message())
				when {
					response.isSuccessful -> {
						val listing = response.body()?.data
						val posts = listing?.children?.map { it.data }
						callback.onResult(posts ?: listOf(), listing?.before, listing?.after)
					}
				}
			} catch (e: Exception) {
				Log.e("PostsDataSource", "Failed to fetch data!")
			}
		}
	}

	override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, Post>) {
		mScope.launch {
			try {
				val response = mRepository.fetchPosts()
				when {
					response.isSuccessful -> {
						val listing = response.body()?.data
						val posts = listing?.children?.map { it.data }
						callback.onResult(posts ?: listOf(), listing?.after)
					}
				}
			} catch (e: Exception) {
				Log.e("PostsDataSource", "Failed to fetch data!")
			}
		}
	}

	override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, Post>) {
		mScope.launch {
			try {
				val response = mRepository.fetchPosts()
				when {
					response.isSuccessful -> {
						val listing = response.body()?.data
						val posts = listing?.children?.map { it.data }
						callback.onResult(posts ?: listOf(), listing?.after)
					}
				}
			} catch (e: Exception) {
				Log.e("PostsDataSource", "Failed to fetch data!")
			}
		}
	}

	override fun invalidate() {
		super.invalidate()
		mJob.cancel()
	}

	companion object {
		private const val TAG = "POSTS DS"
	}

}