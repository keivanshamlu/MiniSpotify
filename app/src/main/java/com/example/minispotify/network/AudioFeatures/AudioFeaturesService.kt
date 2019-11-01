package com.example.minispotify.network.AudioFeatures

import com.example.minispotify.model.trackDetails.AudioFeaturesResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * request to the spotify servers for more information about a track
 */
interface AudioFeaturesService {

    @GET("v1/audio-features/{id}")
    suspend fun getAudioFeatures(@Path("id") trackId : String) : Response<AudioFeaturesResult>
}