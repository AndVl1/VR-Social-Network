package ru.bmstu.iu9.vrsocialnetwork.data.model

data class ApiListing(
	val children: List<PostContainer>,
	val before: String?,
	val after: String?
)