package com.example.minispotify.viewModels

import androidx.lifecycle.*
import com.example.minispotify.model.trackDetails.AudioFeaturesResult
import com.example.minispotify.repository.TrackDetailsRepository
import com.example.minispotify.util.Resource
import com.example.minispotify.util.Status
import javax.inject.Inject

class TrackDetailsViewModel
@Inject constructor(var trackDetailsRepository: TrackDetailsRepository) : ViewModel() , LifecycleObserver {


    val audioFeaturesLiveData = MediatorLiveData<Resource<AudioFeaturesResult>>()
    val isConnected = MediatorLiveData<Boolean>()


    /**
     * we clean up this fragements data because this
     * fragment should not hold any data
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY) // Optional if you want to cleanup references
    fun cleanUp() {
        // this method will respond to destroy event of our Lifecycle owner (activity/fragment in our case)
        // Clean up code
        audioFeaturesLiveData.value = null
        isConnected.value = null
    }

    // Assign our LifecyclerObserver to LifecycleOwner
    fun addLocationUpdates(lifecycle: Lifecycle){
        lifecycle.addObserver(this)
    }
    init {

        audioFeaturesLiveData.addSource(trackDetailsRepository.getAudioFeaturesLiveData){

            audioFeaturesLiveData.value = it
            handleAudiofeaturesResult(it)
        }

        isConnected.addSource(trackDetailsRepository.isConnected){

            isConnected.value = it
        }
    }

    fun getAudioFeatures(trackId : String){

        trackDetailsRepository.getAudioFeatures(trackId)
    }

    fun handleAudiofeaturesResult(result : Resource<AudioFeaturesResult>){

        when(result.status){

            Status.SUCCESS -> {
                handleSuccess()
            }
            Status.ERROR -> {
                handleError()
            }
            Status.LOADING -> {
                handleLoading()
            }
        }
    }
    fun handleError(){

    }

    fun handleLoading(){

    }
    fun handleSuccess(){

    }
}