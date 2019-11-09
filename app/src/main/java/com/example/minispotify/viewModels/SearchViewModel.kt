package com.example.minispotify.viewModels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minispotify.model.search.SearchResult
import com.example.minispotify.repository.SearchRepository
import com.example.minispotify.util.Resource
import com.example.minispotify.util.Status
import javax.inject.Inject

class SearchViewModel
@Inject constructor(val searchRepository: SearchRepository) : ViewModel(){

    val tracksresultLiveData = MediatorLiveData<Resource<SearchResult>>()
    val lastSearchedText = MutableLiveData<String>()
    val isConnected = MediatorLiveData<Boolean>()

    init {

        tracksresultLiveData.addSource(searchRepository.tracksLiveData){

            handleSearchResult(it)
        }
        isConnected.addSource(searchRepository.isConnected){

            isConnected.value = it
        }
    }

    fun handleSearchResult(result :Resource<SearchResult>){

        if(tracksresultLiveData.value != result){
            tracksresultLiveData.value = result

            when(result.status){

                Status.ERROR -> {
                    handleError()
                }
                Status.LOADING -> {
                    handleLoading()
                }
                Status.SUCCESS -> {
                    handleSuccess()
                }
            }
        }

    }

    fun searchSpotify(searchedText :String){

            lastSearchedText.value = searchedText
            searchRepository.searchInSpotify(searchedText)

    }
    fun handleError(){

    }

    fun handleLoading(){

    }
    fun handleSuccess(){


    }
}