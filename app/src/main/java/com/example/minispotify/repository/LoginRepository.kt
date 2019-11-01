package com.example.minispotify.repository

import androidx.lifecycle.MediatorLiveData
import com.example.minispotify.SessionManager
import com.example.minispotify.model.User
import com.example.minispotify.util.AuthResource
import javax.inject.Inject

class LoginRepository
@Inject constructor(var sessionManager: SessionManager){

    // we observe user current state and we gon use it later
    val cachedUserLiveData = MediatorLiveData<AuthResource<User>>()

    init {

        cachedUserLiveData.addSource(sessionManager.cachedUserLiveData){

            cachedUserLiveData.value = it
        }
    }

    fun setStateLoading(){

        sessionManager.setStateLoading()
    }

    fun setStateError(errorMessage : String){

        sessionManager.setStateError(errorMessage)
    }
    fun setUser(user : User){

        sessionManager.setUser(user)

    }
}