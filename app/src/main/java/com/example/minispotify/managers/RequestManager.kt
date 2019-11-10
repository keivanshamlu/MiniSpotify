package com.example.minispotify.managers

import androidx.lifecycle.MutableLiveData
import com.example.minispotify.model.Request
import com.example.minispotify.util.RequestResource
import javax.inject.Inject
import javax.inject.Singleton


/**
 * holds last failed request and contains the
 * connection state of device , implementation
 * of login of request parking is in repository
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


        cachedRequest.value = request
    }

    fun clearRequests(){

        cachedRequest.value = null
    }

}