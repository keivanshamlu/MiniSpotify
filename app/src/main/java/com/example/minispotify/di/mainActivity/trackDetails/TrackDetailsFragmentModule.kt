package com.example.minispotify.di.mainActivity.trackDetails

import android.app.Application
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
    fun provideTrackDetailsRepository(audioFeaturesService: AudioFeaturesService , application: Application): TrackDetailsRepository {

        return TrackDetailsRepository(audioFeaturesService , application)
    }

    @Provides
    fun provideAudioDetailsService(retrofit: Retrofit) : AudioFeaturesService{

       return retrofit.create(AudioFeaturesService::class.java)
    }

}