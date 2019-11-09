package com.example.minispotify.di.mainActivity.login

import com.example.minispotify.managers.RequestManager
import com.example.minispotify.managers.SessionManager
import com.example.minispotify.repository.LoginRepository
import dagger.Module
import dagger.Provides

/**
 * provide objects in loginFragment scope
 */
@Module
class LoginFragmentModule {

    @Provides
    fun provideLoginRepository(sessionManager: SessionManager , requestManager: RequestManager): LoginRepository {

        return LoginRepository(sessionManager , requestManager)
    }

}