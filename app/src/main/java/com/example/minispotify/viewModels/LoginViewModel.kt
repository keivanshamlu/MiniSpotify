package com.example.minispotify.viewModels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.minispotify.model.User
import com.example.minispotify.repository.LoginRepository
import com.example.minispotify.util.AuthResource
import javax.inject.Inject

class LoginViewModel
@Inject constructor(val loginRepository: LoginRepository) : ViewModel() {

    val cachedUserLiveData = MediatorLiveData<AuthResource<User>>()

    init {

        cachedUserLiveData.addSource(loginRepository.cachedUserLiveData){

            cachedUserLiveData.value = it
        }
    }


    fun setStateLoading(){

        loginRepository.setStateLoading()
    }

    fun setStateError(errorMessage : String){

        loginRepository.setStateError(errorMessage)
    }
    fun setUser(user : User){

        loginRepository.setUser(user)

    }
}