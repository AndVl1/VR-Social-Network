package ru.bmstu.iu9.vrsocialnetwork.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "posts")
data class Post (
	@PrimaryKey(autoGenerate = true)
	@SerializedName("id")
	val id: Long,
	@SerializedName("authorId")
	var authorID: String,
	@SerializedName("authorName")
	val authorName: String,
	@SerializedName("text")
	val text: String,
	@SerializedName("imageLink")
	var imageLink: String
)
