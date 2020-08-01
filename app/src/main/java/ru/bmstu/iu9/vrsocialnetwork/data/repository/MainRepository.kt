package ru.bmstu.iu9.vrsocialnetwork.data.repository

import android.util.Log
import androidx.paging.DataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ru.bmstu.iu9.vrsocialnetwork.data.api.ApiService
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post
import ru.bmstu.iu9.vrsocialnetwork.data.room.PostsDao
import javax.inject.Inject

class MainRepository @Inject constructor(private val mApiService: ApiService, private val mPostsDao: PostsDao) {
	suspend fun getPosts() = mApiService.fetchPosts()
//	fun getLoadedPosts() = mPostsDao.loadAllAsDataSource()

	fun getLoadedPosts(scope: CoroutineScope) : DataSource.Factory<Int, Post> {
		scope.launch {
			val posts = mPostsDao.loadAll()
			Log.d(TAG, "${posts.size}")
		}
		return mPostsDao.loadAllAsDataSource()
	}
	suspend fun addPost(post: Post) {
		mPostsDao.insert(post)
	}
	companion object {
		private const val TAG = "MAIN REP"
	}
}