package com.example.minispotify.util

import com.example.minispotify.SessionManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

/**
 * we user this to perform a oath athentication
 */
class BasicAuthInterceptor
@Inject constructor(var sessionManager: SessionManager)  : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", sessionManager.getAccessToken()).build()
        return chain.proceed(authenticatedRequest)
    }
}