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

	suspend fun addPost(post: Post, scope: CoroutineScope): MutableLiveData<Boolean> {
		return withContext(scope.coroutineContext) {
			mIOScope.launch {
				mPostsDao.insert(post)
			}.join()
			val file = Uri.fromFile(File(post.imageLink))
			val res = MutableLiveData<Boolean>()
			try {
				var link = ""
				mIOScope.launch {
					val ref = mStorage.reference
						.child("posts/${file.lastPathSegment}")

					ref.putFile(file)
						.await()

//					link = ref.downloadUrl.result.toString()
//					post.imageLink = link
				}.join()

			} catch (e: Exception) {
				Log.e(TAG, e.message)
				res.value = false
			}
			res.value = true
			res
		}
	}

	companion object {
		private const val TAG = "MAIN REP"
	}
}