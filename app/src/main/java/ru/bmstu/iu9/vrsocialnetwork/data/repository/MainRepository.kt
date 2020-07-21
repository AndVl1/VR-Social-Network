package ru.bmstu.iu9.vrsocialnetwork.data.repository

import ru.bmstu.iu9.vrsocialnetwork.data.PostsDataSource
import ru.bmstu.iu9.vrsocialnetwork.data.api.ApiService
import javax.inject.Inject

class MainRepository @Inject constructor(private val mApiService: ApiService) {
	suspend fun getPosts() = mApiService.fetchPosts()
}