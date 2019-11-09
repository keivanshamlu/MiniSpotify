package com.example.minispotify.repository

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.minispotify.managers.RequestManager
import com.example.minispotify.model.Request
import com.example.minispotify.model.trackDetails.AudioFeaturesResult
import com.example.minispotify.network.AudioFeatures.AudioFeaturesService
import com.example.minispotify.util.RequestDetector
import com.example.minispotify.util.RequestResource
import com.example.minispotify.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrackDetailsRepository
@Inject constructor(var audioFeaturesService: AudioFeaturesService , var requestManager: RequestManager) {

    val getAudioFeaturesLiveData = MutableLiveData<Resource<AudioFeaturesResult>>()

    val isConnected = MediatorLiveData<Boolean>()

    init {

        isConnected.addSource(requestManager.internetConnected){

            isConnected.value = it
            networkStateChanged(it)
        }

    }

    /**
     * when connection state is idle to have a request
     * again , it call getAudioFeatures
     */
    fun networkStateChanged(isConnected : Boolean) : Boolean{



        if(isConnected){

            if(requestManager.cachedRequest.value?.status == RequestDetector.AUDIO_FEATURES){

                getAudioFeatures(requestManager.cachedRequest.value?.data!!.trackId)
            }
        }

        return isConnected
    }

    /**
     * requests for audio features
     */
    fun getAudioFeatures(trackId: String) {

        if(requestManager.internetConnected.value!!){

            // we set resource state to loading to react showing progressbar in view
            getAudioFeaturesLiveData.value = Resource.loading(null)

            CoroutineScope(IO).launch {

                try {

                    var registerResult = audioFeaturesService.getAudioFeatures(trackId)


                    withContext(Main) {

                        if (registerResult!!.isSuccessful) {

                            // if request is successfull , we fill Resource class and set it to livedata
                            getAudioFeaturesLiveData.value = Resource.success(registerResult.body())
                            requestManager.clearRequests()
                        } else {

                            // if request is unSuccessfull , we fill Resource class and set it to livedata
                            getAudioFeaturesLiveData.value =
                                Resource.error(registerResult.message(), null)
                            requestManager.setRequest(RequestResource.audiofeatures(Request(trackId = trackId)))
                        }
                    }

                } catch (e: Exception) {
                    withContext(Main) {

                        getAudioFeaturesLiveData.value = Resource.error("conenction_faled", null)
                        requestManager.setRequest(RequestResource.audiofeatures(Request(trackId = trackId)))
                    }
                }
            }
        }else{

            requestManager.setRequest(RequestResource.audiofeatures(Request(trackId = trackId)))
        }

    }
}