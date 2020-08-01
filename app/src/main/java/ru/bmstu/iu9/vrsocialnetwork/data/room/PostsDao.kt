package ru.bmstu.iu9.vrsocialnetwork.data.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ru.bmstu.iu9.vrsocialnetwork.data.model.Post

@Dao
interface PostsDao {
    @Query("SELECT * FROM posts ORDER BY id")
    fun loadAllAsDataSource(): DataSource.Factory<Int, Post>

    @Query("SELECT * FROM posts ORDER BY id")
    suspend fun loadAll(): List<Post>

    @Query("DELETE FROM posts")
    suspend fun clear()

    @Insert
    suspend fun insert(post: Post)

    @Delete
    suspend fun delete(post: Post)
}