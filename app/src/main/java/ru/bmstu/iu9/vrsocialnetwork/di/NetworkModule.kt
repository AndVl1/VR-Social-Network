package ru.bmstu.iu9.vrsocialnetwork.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import ru.bmstu.iu9.vrsocialnetwork.data.api.RetrofitBuilder
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

	@Provides
	@Singleton
	fun provideRetrofit() = RetrofitBuilder.apiService
}