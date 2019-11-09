package com.example.minispotify.managers

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.minispotify.model.User
import com.example.minispotify.util.AuthResource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * holds state of user in app lifeTime , most important implementation of this class is in BaseFragment
 */
@Singleton
class SessionManager
@Inject
constructor(val application: Application) {

    val cachedUserLiveData = MutableLiveData<AuthResource<User>>()


    fun setStateLoading(){

        cachedUserLiveData.value = AuthResource.loading("Loading" , null)
    }

    fun setStateError(errorMessage : String){

        cachedUserLiveData.value = AuthResource.error(errorMessage , null)
    }
    fun setUser(user : User){
        Log.e("Tokennnnn " , user.token)
        cachedUserLiveData.value = AuthResource.authenticated(user)

    }

    fun logOutUser(){


        cachedUserLiveData.value = AuthResource.unAthenticated("" , null)
    }

    fun getAccessToken() :String?{

        return cachedUserLiveData.value?.data?.token
    }

}