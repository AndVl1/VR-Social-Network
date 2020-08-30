package ru.bmstu.iu9.vrsocialnetwork.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import ru.bmstu.iu9.vrsocialnetwork.data.room.PostsDatabase
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesPostDao(app: Application) = Room.databaseBuilder(app, PostsDatabase::class.java, "Posts Database")
        .build()
        .postsDao()
}