package com.example.minispotify.viewModels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.minispotify.model.trackDetails.AudioFeaturesResult
import com.example.minispotify.repository.TrackDetailsRepository
import com.example.minispotify.util.Resource
import com.example.minispotify.util.Status
import javax.inject.Inject

class TrackDetailsViewModel
@Inject constructor(var trackDetailsRepository: TrackDetailsRepository) : ViewModel(){

    val audioFeaturesLiveData = MediatorLiveData<Resource<AudioFeaturesResult>>()

    init {

        audioFeaturesLiveData.addSource(trackDetailsRepository.getAudioFeaturesLiveData){

            audioFeaturesLiveData.value = it
            handleAudiofeaturesResult(it)
        }
    }

    fun getAudioFeatures(trackId : String){

        trackDetailsRepository.getAudioFeatures(trackId)
    }

    fun handleAudiofeaturesResult(result : Resource<AudioFeaturesResult>){

        when(result.status){

            Status.SUCCESS -> {}
            Status.ERROR -> {}
            Status.LOADING -> {}
        }
    }
}