package com.example.minispotify.network.search

import com.example.minispotify.model.search.SearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * searchs spotify according to search
 * text and Type of element we are searching for
 */
interface SearchSpotify {

    @GET("v1/search")
    suspend fun searchSpotify(
        @Query("q") searchText :String,
        @Query("type") type :String

    ) : Response<SearchResult>
}