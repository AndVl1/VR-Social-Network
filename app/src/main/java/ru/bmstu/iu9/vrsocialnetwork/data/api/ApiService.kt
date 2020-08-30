package ru.bmstu.iu9.vrsocialnetwork.data.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import ru.bmstu.iu9.vrsocialnetwork.data.model.*

interface ApiService {

	/** Paging */
	@GET("/posts")
	suspend fun fetchPosts(
		@Query("limit") loadSize: Int = 30,
		@Query("after") after: String? = null,
		@Query("before") before: String? = null
	): Response<ApiResponse>

	/** Normal list */
	@GET("/posts")
	suspend fun getPosts(): List<Post>

	@POST("/posts")
	suspend fun addPost(@Body post: Post): Call<User>

	@POST("/models")
	suspend fun addModel(@Body model: PostModel): Call<Int>

	@POST("/clients")
	suspend fun addClient(@Body client: User)
}