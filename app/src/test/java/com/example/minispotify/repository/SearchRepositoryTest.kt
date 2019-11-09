package com.example.minispotify.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minispotify.managers.RequestManager
import com.example.minispotify.model.search.SearchResult
import com.example.minispotify.network.search.SearchSpotify
import com.example.minispotify.util.InstantExecutorExtension
import com.example.minispotify.util.LiveDataTestUtil
import com.example.minispotify.util.Resource
import kotlinx.coroutines.newSingleThreadContext
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExtendWith(InstantExecutorExtension::class)
class SearchRepositoryTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var searchRepository: SearchRepository

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Mock
    lateinit var searchSpotify: SearchSpotify

    @Mock
    lateinit var requestManager: RequestManager

    @BeforeEach
    fun initBeforeEach() {

        MockitoAnnotations.initMocks(this)

        searchRepository = SearchRepository(searchSpotify , requestManager)
    }


    @Test
    fun resourceLiveDataTest() {

        var resource = Resource.loading(null)

        var resourceLiveData = LiveDataTestUtil<Resource<SearchResult>>()

        searchRepository.tracksLiveData.value = resource

        val observedData = resourceLiveData.getValue(searchRepository.tracksLiveData)

        assertEquals(resource, observedData)
    }
}