package com.example.minispotify.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.minispotify.R
import com.example.minispotify.model.trackDetails.AudioFeaturesResult
import com.example.minispotify.network.AudioFeatures.AudioFeaturesService
import com.example.minispotify.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TrackDetailsRepository
@Inject constructor(var audioFeaturesService: AudioFeaturesService , var application : Application) {

    val getAudioFeaturesLiveData = MutableLiveData<Resource<AudioFeaturesResult>>()

    fun getAudioFeatures(trackId: String) {

        // we set resource state to loading to react showing progressbar in view
        getAudioFeaturesLiveData.value = Resource.loading(null)

        CoroutineScope(IO).launch {

            try {

                var registerResult = audioFeaturesService.getAudioFeatures(trackId)


                withContext(Main) {

                    if (registerResult!!.isSuccessful) {

                        // if request is successfull , we fill Resource class and set it to livedata
                        getAudioFeaturesLiveData.value = Resource.success(registerResult.body())
                    } else {

                        // if request is unSuccessfull , we fill Resource class and set it to livedata
                        getAudioFeaturesLiveData.value =
                            Resource.error(registerResult.message(), null)
                    }
                }

            } catch (e: Exception) {
                withContext(Main) {

                    getAudioFeaturesLiveData.value = Resource.error(application.getString(R.string.conenction_faled), null)
                }
            }
        }
    }
}