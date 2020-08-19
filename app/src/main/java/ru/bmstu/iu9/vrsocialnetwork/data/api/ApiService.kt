package ru.bmstu.iu9.vrsocialnetwork.data.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.bmstu.iu9.vrsocialnetwork.data.model.ApiResponse
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post
import ru.bmstu.iu9.vrsocialnetwork.data.model.PostModel
import ru.bmstu.iu9.vrsocialnetwork.data.model.User

interface ApiService {

	@GET()
	suspend fun fetchPosts(
		@Query("limit") loadSize: Int = 30,
		@Query("after") after: String? = null,
		@Query("before") before: String? = null
	): Response<ApiResponse>

	@POST("/posts")
	suspend fun addPost(@Body post: Post): Call<User>

	@POST("/models")
	suspend fun addModel(@Body model: PostModel): Call<Int>
}