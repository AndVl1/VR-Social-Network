package ru.bmstu.iu9.vrsocialnetwork.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post

@Database(entities = [Post::class], version = 1)
abstract class PostsDatabase: RoomDatabase() {
    abstract fun postsDao(): PostsDao
}