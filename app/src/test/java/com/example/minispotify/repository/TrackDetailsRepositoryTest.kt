package com.example.minispotify.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minispotify.managers.RequestManager
import com.example.minispotify.model.trackDetails.AudioFeaturesResult
import com.example.minispotify.network.AudioFeatures.AudioFeaturesService
import com.example.minispotify.util.InstantExecutorExtension
import com.example.minispotify.util.LiveDataTestUtil
import com.example.minispotify.util.Resource
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExtendWith(InstantExecutorExtension::class)
class TrackDetailsRepositoryTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var trackDetailsRepository: TrackDetailsRepository


    @Mock
    lateinit var audioFeaturesService: AudioFeaturesService
    @Mock
    lateinit var requestManager: RequestManager

    @BeforeEach
    fun initBeforeEach() {

        MockitoAnnotations.initMocks(this)

        trackDetailsRepository = TrackDetailsRepository(audioFeaturesService , requestManager)
    }



    @Test
    fun resourceLiveDataTest() {

        var resource = Resource.loading(null)

        var resourceLiveData =
            LiveDataTestUtil<Resource<AudioFeaturesResult>>()

        trackDetailsRepository.getAudioFeaturesLiveData.value = resource

        val observedData = resourceLiveData.getValue(trackDetailsRepository.getAudioFeaturesLiveData)

        assertEquals(resource, observedData)
    }
}