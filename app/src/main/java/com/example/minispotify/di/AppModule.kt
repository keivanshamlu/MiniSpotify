package com.example.daggerkotlinn.di

import android.app.Application
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.minispotify.SessionManager
import com.example.minispotify.util.BasicAuthInterceptor
import com.example.minispotify.util.Constans.BASE_URL
import com.example.minispotify.util.Constans.CLIENT_ID
import com.example.minispotify.util.Constans.REDIRECT_URI
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * we provide objects that are alive in lifeTime of application
 */
@Module
class AppModule {

    //test
    //i use this simple providation to setUp dagger
    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
            .placeholderOf(com.example.minispotify.R.drawable.default_image)
            .error(com.example.minispotify.R.drawable.default_image)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(application: Application, requestOptions: RequestOptions): RequestManager {
        return Glide.with(application)
            .setDefaultRequestOptions(requestOptions)
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }


    /**
     * i send
     * @param sessionManager
     * as parameter to this method , for Oath Athentication
     * , we need to create a AuthInterceptor and we use accessToken
     * there , in this project , sessionManager have the
     * resonsibility to provide user datas in whole project
     * and i provide it in appComponent so sessionManager is avaible
     * here , we pass it through BasicInterceptor and
     * interceptor uses the accessToken
     */
    @Singleton
    @Provides
    fun provideOkHttp(sessionManager: SessionManager) :OkHttpClient{
        return OkHttpClient.Builder()
            .connectTimeout(7 , TimeUnit.SECONDS)
            .writeTimeout(7 , TimeUnit.SECONDS)
            .readTimeout(7 , TimeUnit.SECONDS)
            .addInterceptor(BasicAuthInterceptor(sessionManager))
            .build()
    }

    /**
     * we provide AuthenticationRequest here and
     * we send spotify developer CLIENT_ID , also
     * a redirect link to application to return into
     * app after athentication
     */
    @Singleton
    @Provides
    fun provideAuthenticationRequest(): AuthenticationRequest {

        val builder = AuthenticationRequest.Builder(
            CLIENT_ID,
            AuthenticationResponse.Type.TOKEN,
            REDIRECT_URI
        )

        builder.setScopes(arrayOf("streaming"))
        return builder.build()
    }


}