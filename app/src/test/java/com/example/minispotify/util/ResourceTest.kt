package com.example.minispotify.util

import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ResourceTest {

    @Mock
    lateinit var resource: Resource<String>

    @BeforeEach
    fun initBeforeEach() {

        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun create_verify() {

        resource = Resource.loading("isLoading")

        assertEquals(resource.data , "isLoading")
    }

    @Test
    fun create_change_verify() {

        resource = Resource.error("error1" , "ErrorData")

        resource = Resource.error("error2" , "ErrorData2")

        assertEquals(resource.message , "error2")
        assertEquals(resource.data , "ErrorData2")
    }


}