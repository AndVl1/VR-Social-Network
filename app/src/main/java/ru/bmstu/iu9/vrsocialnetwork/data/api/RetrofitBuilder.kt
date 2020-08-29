package ru.bmstu.iu9.vrsocialnetwork.data.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
	private const val BASE_URL = "http://188.68.106.226:42000/"
//	private const val BASE_URL = "http://10.0.2.2:8080/"

	private fun getRetrofit() : Retrofit {
		return Retrofit.Builder()
			.baseUrl(BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.addCallAdapterFactory(CoroutineCallAdapterFactory())
			.build()
	}

	val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}