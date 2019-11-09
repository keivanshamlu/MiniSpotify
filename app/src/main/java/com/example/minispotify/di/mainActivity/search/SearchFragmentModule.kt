package com.example.minispotify.di.mainActivity.search

import com.example.minispotify.managers.RequestManager
import com.example.minispotify.network.search.SearchSpotify
import com.example.minispotify.repository.SearchRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * provide objects in SearchFragment scope
 */
@Module
class SearchFragmentModule {

    @Provides
    fun provideSearchRepository( searchSpotify: SearchSpotify , requestManager : RequestManager ): SearchRepository {

        return SearchRepository( searchSpotify , requestManager )
    }

    @Provides
    fun provideSearchServiceRepository(retrofit: Retrofit): SearchSpotify {

        return retrofit.create(SearchSpotify::class.java)
    }


}