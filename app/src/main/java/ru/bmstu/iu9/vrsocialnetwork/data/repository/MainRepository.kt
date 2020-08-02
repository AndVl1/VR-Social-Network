package ru.bmstu.iu9.vrsocialnetwork.data.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ru.bmstu.iu9.vrsocialnetwork.data.api.ApiService
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post
import ru.bmstu.iu9.vrsocialnetwork.data.room.PostsDao
import java.io.File
import javax.inject.Inject

class MainRepository @Inject constructor(
	private val mApiService: ApiService,
	private val mPostsDao: PostsDao
) {
	private val mStorage = FirebaseStorage.getInstance()
	private val mIOScope = CoroutineScope(Dispatchers.IO)

	suspend fun getPosts() = mApiService.fetchPosts()
//	fun getLoadedPosts() = mPostsDao.loadAllAsDataSource()

	fun getLoadedPosts(): DataSource.Factory<Int, Post> {
		return mPostsDao.loadAllAsDataSource()
	}

	suspend fun getLoadedPosts(scope: CoroutineScope) {
		scope.launch {
			mPostsDao.loadAll()
		}
	}

	suspend fun addPost(post: Post, scope: CoroutineScope): Boolean {
		return withContext(scope.coroutineContext) {
			mIOScope.launch {
				mPostsDao.insert(post)
			}.join()
			val file = Uri.fromFile(File(post.imageLink))
			var res: Boolean
			try {
				var link: String
				mIOScope.launch {

					val ref = mStorage.reference
						.child("posts/${file.lastPathSegment}")

					ref.putFile(file)
						.await()

					link = ref.downloadUrl.await().encodedPath.toString()

					post.imageLink = link
					Log.d(TAG, post.toString())
				}.join()
				res = true
			} catch (e: Exception) {
				Log.e(TAG, e.message ?: "err")
				res = false
			}
			res
		}
	}

	companion object {
		private const val TAG = "MAIN REP"
	}
}