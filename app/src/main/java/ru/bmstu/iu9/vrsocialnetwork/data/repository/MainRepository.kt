package ru.bmstu.iu9.vrsocialnetwork.data.repository

import android.net.Uri
import android.util.Log
import androidx.paging.DataSource
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.await
import ru.bmstu.iu9.vrsocialnetwork.data.api.ApiService
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post
import ru.bmstu.iu9.vrsocialnetwork.data.model.PostModel
import ru.bmstu.iu9.vrsocialnetwork.data.model.User
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

	suspend fun addModel(model: PostModel, scope: CoroutineScope): Boolean {
		return withContext(scope.coroutineContext) {

			val folder = File(model.imagesLink)
			var res: Boolean
			try {
				var link: String
				mIOScope.launch {

					val ref = mStorage.reference
						.child("userPhotos/")
						.child("${folder.name}/")

					for (file in folder.listFiles()) {
						val dest = ref.child(file.name)
						dest.putFile(Uri.fromFile(file))
							.await()
					}

					model.imagesLink = "userPhotos/${folder.name}"
					Log.d(TAG, model.toString())
					try {
						mApiService.addModel(model).await()
					} catch (e: Exception) {
						e.printStackTrace()
					}
				}.join()
				res = true
			} catch (e: Exception) {
				Log.e(TAG, e.message ?: "err")
				res = false
			}
			res
		}
	}

	suspend fun addClient(client: User): Boolean {
		return withContext(mIOScope.coroutineContext) {
			val res = true
			try {
				mApiService.addClient(client)
			} catch (e: Exception) {
				Log.e(TAG, e.stackTraceToString())
			}
			res
		}
	}

	suspend fun addPost(post: Post, scope: CoroutineScope): Boolean {
		return withContext(scope.coroutineContext) {
			mIOScope.launch {
				mPostsDao.insert(post)
			}.join()
			val folder = File(post.imageLink)
			var res: Boolean
			try {
				var link: String
				mIOScope.launch {

					val ref = mStorage.reference
						.child("userPhotos/")
						.child("${folder.name}/")

					for (file in folder.listFiles()) {
						val dest = ref.child(file.name)
						dest.putFile(Uri.fromFile(file))
							.await()
					}

					post.imageLink = "userPhotos/${folder.name}"
					Log.d(TAG, post.toString())
					try {
						mApiService.addPost(post)
					} catch (e: Exception) {
						e.printStackTrace()
					}
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