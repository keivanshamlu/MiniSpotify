package com.example.minispotify.util


data class RequestResource<out T>(val status: RequestDetector, val data: T?) {
    companion object {
        fun <T> search(data: T?): RequestResource<T> {
            return RequestResource(RequestDetector.SEARCH, data)
        }

        fun <T> audiofeatures(data: T?): RequestResource<T> {
            return RequestResource(RequestDetector.AUDIO_FEATURES, data)
        }




    }
}
enum class RequestDetector {
    SEARCH,
    AUDIO_FEATURES
}