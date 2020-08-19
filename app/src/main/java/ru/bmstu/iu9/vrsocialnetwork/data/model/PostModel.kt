package ru.bmstu.iu9.vrsocialnetwork.data.model

import com.google.gson.annotations.SerializedName
import ru.bmstu.iu9.vrsocialnetwork.data.SensorMap

data class PostModel(
	@SerializedName("authorId")
	val uID: String,
	@SerializedName("sensors")
	val sensors: SensorMap,
	@SerializedName("modelLink")
	var imagesLink: String
)