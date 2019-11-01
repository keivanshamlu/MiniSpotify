package com.example.minispotify.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.minispotify.R
import com.example.minispotify.model.search.SearchResult
import com.example.minispotify.network.search.SearchSpotify
import com.example.minispotify.util.Constans.TRACK_TYPE
import com.example.minispotify.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository
@Inject constructor(var searchSpotify: SearchSpotify , var application: Application) {

    val tracksLiveData = MutableLiveData<Resource<SearchResult>>()


    fun searchInSpotify(searchText :String) {

        // we set resource state to loading to react showing progressbar in view
        tracksLiveData.value = Resource.loading(null)
        CoroutineScope(IO).launch {

            try {
                var registerResult = searchSpotify.searchSpotify( searchText, TRACK_TYPE)


                withContext(Main) {

                    if (registerResult!!.isSuccessful) {

                        // if request is successfull , we fill Resource class and set it to livedata
                        tracksLiveData.value = Resource.success(registerResult.body())
                    } else {

                        // if request is unSuccessfull , we fill Resource class and set it to livedata
                        tracksLiveData.value = Resource.error(registerResult.code().toString(), null)
                    }
                }

            } catch (e: Exception) {
                withContext(Main) {

                    tracksLiveData.value = Resource.error(application.getString(R.string.conenction_faled), null)
                }
            }
        }
    }
}