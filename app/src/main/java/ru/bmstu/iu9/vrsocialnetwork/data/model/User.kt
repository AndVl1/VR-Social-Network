package ru.bmstu.iu9.vrsocialnetwork.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class User(
    @SerializedName("id")
    val uId: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("email")
    val email: String = "",
    var isAuthenticated: Boolean? = null,
    var isNew: Boolean? = null,
    var isCreated: Boolean? = null
)