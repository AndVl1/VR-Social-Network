package ru.bmstu.iu9.vrsocialnetwork.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class User(
    val uId: String = "",
    val name: String = "",
    val email: String = "",
    var isAuthenticated: Boolean? = null,
    var isNew: Boolean? = null,
    var isCreated: Boolean? = null
)