package com.example.minispotify.util

/**
 * wrap a user with authentication user , it is helpfull when we using liveData
 */
data class AuthResource<out T>(val status: AuthStatus, val data: T?, val message: String?) {
    companion object {
        fun <T> authenticated(data: T?): AuthResource<T> {
            return AuthResource(AuthStatus.AUTHENTICATED, data, null)
        }

        fun <T> error(msg: String, data: T?): AuthResource<T> {
            return AuthResource(AuthStatus.ERROR, data, msg)
        }

        fun <T> loading(msg: String, data: T?): AuthResource<T> {
            return AuthResource(AuthStatus.LOADING, data, msg)
        }

        fun <T> unAthenticated(msg: String, data: T?): AuthResource<T> {
            return AuthResource(AuthStatus.LOG_OUT, data, msg)
        }


    }
}
enum class AuthStatus {
    AUTHENTICATED,
    LOG_OUT,
    LOADING,
    ERROR
}