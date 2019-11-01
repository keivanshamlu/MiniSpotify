package com.example.minispotify.model

/**
 * this class contains the current user details
 */
data class User(
    val token: String,
    val loginType: LoginType
)

/**
 * LoginType is how user did authenticate
 */
enum class LoginType{
    FROM_APP,
    FROM_BROWSER
}