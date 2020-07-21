package ru.bmstu.iu9.vrsocialnetwork.data.model

import com.google.gson.annotations.SerializedName

data class Post (
	@SerializedName("name")
	val authorName: String,
	@SerializedName("id")
	val id: Long,
	@SerializedName("profileImage")
	val profileImageLink: String?,
	@SerializedName("text")
	val text: String,
	@SerializedName("image")
	val imageLink: String?
)
