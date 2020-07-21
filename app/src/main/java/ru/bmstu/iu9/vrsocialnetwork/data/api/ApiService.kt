package ru.bmstu.iu9.vrsocialnetwork.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.bmstu.iu9.vrsocialnetwork.data.model.ApiResponse

interface ApiService {

	@GET()
	suspend fun fetchPosts(
		@Query("limit") loadSize: Int = 30,
		@Query("after") after: String? = null,
		@Query("before") before: String? = null
	): Response<ApiResponse>
}