package ru.bmstu.iu9.vrsocialnetwork.data.repository

import ru.bmstu.iu9.vrsocialnetwork.data.api.ApiService
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post
import ru.bmstu.iu9.vrsocialnetwork.data.room.PostsDao
import javax.inject.Inject

class MainRepository @Inject constructor(private val mApiService: ApiService, private val mPostsDao: PostsDao) {
	suspend fun getPosts() = mApiService.fetchPosts()
	fun getLoadedPosts() = mPostsDao.loadAllAsDataSource()
	suspend fun addPost(post: Post) {
		mPostsDao.insert(post)
	}
}