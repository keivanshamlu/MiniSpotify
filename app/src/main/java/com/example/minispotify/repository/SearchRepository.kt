package com.example.minispotify.repository

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.minispotify.managers.RequestManager
import com.example.minispotify.model.Request
import com.example.minispotify.model.search.SearchResult
import com.example.minispotify.network.search.SearchSpotify
import com.example.minispotify.util.Constans.TRACK_TYPE
import com.example.minispotify.util.RequestDetector
import com.example.minispotify.util.RequestResource
import com.example.minispotify.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository
@Inject constructor(var searchSpotify: SearchSpotify , var requestManager: RequestManager ) {

    val tracksLiveData = MutableLiveData<Resource<SearchResult>>()
    val isConnected = MediatorLiveData<Boolean>()

    init {

        isConnected.addSource(requestManager.internetConnected){

            isConnected.value = it
            networkStateChanged(it)
        }

    }


    fun networkStateChanged(isConnected : Boolean) : Boolean{

        if(isConnected){

            if(requestManager.cachedRequest.value?.status == RequestDetector.SEARCH){

                searchInSpotify(requestManager.cachedRequest.value?.data!!.searchText)
            }
        }

        return isConnected
    }

    fun searchInSpotify(searchText :String) {

        if(isConnected.value!!){

            // we set resource state to loading to react showing progressbar in view
            tracksLiveData.value = Resource.loading(null)
            CoroutineScope(IO).launch {

                try {
                    var registerResult = searchSpotify.searchSpotify( searchText, TRACK_TYPE)


                    withContext(Main) {

                        if (registerResult!!.isSuccessful) {

                            // if request is successfull , we fill Resource class and set it to livedata
                                tracksLiveData.value = Resource.success(registerResult.body())
                            requestManager.clearRequests()
                        } else {

                            // if request is unSuccessfull , we fill Resource class and set it to livedata
                            tracksLiveData.value = Resource.error(registerResult.code().toString(), null)
                            requestManager.setRequest(RequestResource.search(Request(searchText = searchText)))
                        }
                    }

                } catch (e: Exception) {
                    withContext(Main) {

                        tracksLiveData.value = Resource.error("conenction_faled", null)
                        requestManager.setRequest(RequestResource.search(Request(searchText = searchText)))
                    }
                }
            }
        }else{

            requestManager.setRequest(RequestResource.search(Request(searchText = searchText)))
        }

    }
}