package com.example.minispotify.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minispotify.model.trackDetails.AudioFeaturesResult
import com.example.minispotify.repository.TrackDetailsRepository
import com.example.minispotify.util.InstantExecutorExtension
import com.example.minispotify.util.LiveDataTestUtil
import com.example.minispotify.util.Resource
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExtendWith(InstantExecutorExtension::class)
class TrackDetailsViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var trackDetailsRepository: TrackDetailsRepository

    @Mock
    lateinit var trackDetailsResultResource: Resource<AudioFeaturesResult>

    @Mock
    lateinit var trackDetailsResult: AudioFeaturesResult

    lateinit var trackDetailsViewModel: TrackDetailsViewModel

    @BeforeEach
    fun initBeforeEach() {

        MockitoAnnotations.initMocks(this)

        trackDetailsViewModel = TrackDetailsViewModel(trackDetailsRepository)
    }

    @Test
    fun getAudioFeaturesLiveData() {

        // Arrange
        var resourceLiveData = LiveDataTestUtil<Resource<AudioFeaturesResult>>()
        // Act
        trackDetailsViewModel.audioFeaturesLiveData.value = trackDetailsResultResource
        // Assert
        val resultLiveDataValue = resourceLiveData.getValue(trackDetailsViewModel.audioFeaturesLiveData)
        assertEquals(resultLiveDataValue, trackDetailsResultResource)
    }

    @Test
    fun getAudioFeatures() {

        // Act
        trackDetailsViewModel.getAudioFeatures("trackId")
        // Assert
        verify(trackDetailsRepository).getAudioFeatures("trackId")
    }

    @Test
    fun handleAudiofeaturesResult() {

        // Arrang
        var spyDetailsViewModel = Mockito.spy<TrackDetailsViewModel>(TrackDetailsViewModel(trackDetailsRepository))
        var trackDetailsResultSuccess = Resource.success(trackDetailsResult)

        // Act
        spyDetailsViewModel.handleAudiofeaturesResult(trackDetailsResultSuccess)

        // Assert
        verify(spyDetailsViewModel).handleSuccess()

        // Arrang
        var trackDetailsResultError = Resource.error("" , null)

        // Act
        spyDetailsViewModel.handleAudiofeaturesResult(trackDetailsResultError)

        // Assert
        verify(spyDetailsViewModel).handleError()

        // Arrang
        var trackDetailsResultLoading = Resource.loading( null)

        // Act
        spyDetailsViewModel.handleAudiofeaturesResult(trackDetailsResultLoading)

        // Assert
        verify(spyDetailsViewModel).handleLoading()
    }

 }