package com.example.minispotify.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minispotify.model.search.SearchResult
import com.example.minispotify.repository.SearchRepository
import com.example.minispotify.util.InstantExecutorExtension
import com.example.minispotify.util.LiveDataTestUtil
import com.example.minispotify.util.Resource
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations



@ExtendWith(InstantExecutorExtension::class)
class SearchViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var searchViewModel: SearchViewModel

    @Mock
    lateinit var searchResultResource: Resource<SearchResult>
    @Mock
    lateinit var searchResult: SearchResult
    @Mock
    lateinit var searchRepository: SearchRepository

    @BeforeEach
    fun initBeforeEach() {

        MockitoAnnotations.initMocks(this)

        searchViewModel = SearchViewModel(searchRepository)
    }


    @Test
    fun getTracksresultLiveData() {

        // Arrang
        var resourceLiveData = LiveDataTestUtil<Resource<SearchResult>>()

        // Act
        searchViewModel.tracksresultLiveData.value = searchResultResource

        // Assert
        val userLiveDataValue = resourceLiveData.getValue(searchViewModel.tracksresultLiveData)
        assertEquals(userLiveDataValue , searchResultResource)
    }

    @Test
    fun handleSearchResult() {

        // Arrang
        var spySearchViewModel = Mockito.spy<SearchViewModel>(SearchViewModel(searchRepository))
        var searchResultSuccess = Resource.success(searchResult)

        // Act
        spySearchViewModel.handleSearchResult(searchResultSuccess)

        // Assert
        verify(spySearchViewModel).handleSuccess()

        // Arrang
        var searchResultError = Resource.error("" , null)

        // Act
        spySearchViewModel.handleSearchResult(searchResultError)

        // Assert
        verify(spySearchViewModel).handleError()

        // Arrang
        var searchResultLoading = Resource.loading( null)

        // Act
        spySearchViewModel.handleSearchResult(searchResultLoading)

        // Assert
        verify(spySearchViewModel).handleLoading()

    }

    @Test
    fun searchSpotify() {

        // Arrange

        // Act
        searchViewModel.searchSpotify("searchtext")
        // Assert
        verify(searchRepository).searchInSpotify("searchtext")
    }

}