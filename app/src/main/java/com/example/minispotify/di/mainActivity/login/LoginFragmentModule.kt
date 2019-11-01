package com.example.minispotify.di.mainActivity.login

import com.example.minispotify.SessionManager
import com.example.minispotify.repository.LoginRepository
import dagger.Module
import dagger.Provides

/**
 * provide objects in loginFragment scope
 */
@Module
class LoginFragmentModule {

    @Provides
    fun provideLoginRepository(sessionManager: SessionManager): LoginRepository {

        return LoginRepository(sessionManager)
    }

}