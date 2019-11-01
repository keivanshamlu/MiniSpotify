package com.example.minispotify.viewModels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.minispotify.model.search.SearchResult
import com.example.minispotify.repository.SearchRepository
import com.example.minispotify.util.Resource
import com.example.minispotify.util.Status
import javax.inject.Inject

class SearchViewModel
@Inject constructor(val searchRepository: SearchRepository) : ViewModel(){

    val tracksresultLiveData = MediatorLiveData<Resource<SearchResult>>()

    init {

        tracksresultLiveData.addSource(searchRepository.tracksLiveData){

            tracksresultLiveData.value = it
            handleSearchResult(it)
        }
    }

    fun handleSearchResult(result :Resource<SearchResult>){

        when(result.status){

            Status.ERROR -> {}
            Status.LOADING -> {}
            Status.SUCCESS -> {}
        }
    }

    fun searchSpotify(searchedText :String){


        searchRepository.searchInSpotify(searchedText)
    }
}