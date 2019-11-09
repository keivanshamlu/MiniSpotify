package com.example.minispotify.managers

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.minispotify.model.Request
import com.example.minispotify.util.RequestResource
import javax.inject.Inject
import javax.inject.Singleton


/**
 * holds state of user in app lifeTime
 * this hold a failed request and the
 * implementation is in the repository
 */
@Singleton
class RequestManager
@Inject constructor() {

    val cachedRequest = MutableLiveData<RequestResource<Request>>()
    val internetConnected = MutableLiveData<Boolean>()


    fun setConnectionStatus(isConnected : Boolean){

        internetConnected.value = isConnected
    }
    fun setRequest( request : RequestResource<Request>){


        Log.e("keivan test 2 " , request.status.name)
        cachedRequest.value = request
    }

    fun clearRequests(){

        Log.e("keivan test 2 " , "clear")
        cachedRequest.value = null
    }

}