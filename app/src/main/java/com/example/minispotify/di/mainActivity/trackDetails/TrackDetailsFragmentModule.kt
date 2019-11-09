package com.example.minispotify.di.mainActivity.trackDetails

import com.example.minispotify.managers.RequestManager
import com.example.minispotify.network.AudioFeatures.AudioFeaturesService
import com.example.minispotify.repository.TrackDetailsRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * provide objects in TrackDetailsFragment scope
 */
@Module
class TrackDetailsFragmentModule {

    @Provides
    fun provideTrackDetailsRepository(audioFeaturesService: AudioFeaturesService , requestManager: RequestManager): TrackDetailsRepository {

        return TrackDetailsRepository(audioFeaturesService , requestManager)
    }

    @Provides
    fun provideAudioDetailsService(retrofit: Retrofit) : AudioFeaturesService{

       return retrofit.create(AudioFeaturesService::class.java)
    }

}