package com.example.minispotify

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minispotify.managers.SessionManager
import com.example.minispotify.model.LoginType
import com.example.minispotify.model.User
import com.example.minispotify.util.AuthResource
import com.example.minispotify.util.AuthStatus
import com.example.minispotify.util.InstantExecutorExtension
import com.example.minispotify.util.LiveDataTestUtil
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExtendWith(InstantExecutorExtension::class)
class SessionManagerTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var userAuthResource: AuthResource<User>

    @Mock
    lateinit var user :User

    lateinit var sessionManager: SessionManager

    @BeforeEach
    fun init(){

        MockitoAnnotations.initMocks(this)
        sessionManager = SessionManager(application)
    }
    @Test
    fun getCachedUserLiveData() {

        // Arrange
        var authResourceLiveData = LiveDataTestUtil<AuthResource<User>>()
        // Act
        sessionManager.cachedUserLiveData.value = userAuthResource
        // Assert
        var observerdLiveData = authResourceLiveData.getValue(sessionManager.cachedUserLiveData)
        assertEquals(observerdLiveData , userAuthResource)
    }

    @Test
    fun setStateLoading() {

        // Arrange
        var authResourceLiveData = LiveDataTestUtil<AuthResource<User>>()
        // Act
        sessionManager.setStateLoading()
        // Assert
        var observerdLiveData = authResourceLiveData.getValue(sessionManager.cachedUserLiveData)
        assertEquals(observerdLiveData?.status , AuthStatus.LOADING)
    }

    @Test
    fun setStateError() {

        // Arrange
        var authResourceLiveData = LiveDataTestUtil<AuthResource<User>>()
        // Act
        sessionManager.setStateError("errorType")
        // Assert
        var observerdLiveData = authResourceLiveData.getValue(sessionManager.cachedUserLiveData)
        assertEquals(observerdLiveData?.status , AuthStatus.ERROR)
        assertEquals(observerdLiveData?.message , "errorType")
    }

    @Test
    fun setUser() {

        // Arrange
        var authResourceLiveData = LiveDataTestUtil<AuthResource<User>>()
        // Act
        sessionManager.setUser(user)
        // Assert
        var observerdLiveData = authResourceLiveData.getValue(sessionManager.cachedUserLiveData)
        assertEquals(observerdLiveData?.status , AuthStatus.AUTHENTICATED)
        assertEquals(observerdLiveData?.data , user)
    }

    @Test
    fun logOutUser() {

        // Arrange
        var authResourceLiveData = LiveDataTestUtil<AuthResource<User>>()
        // Act
        sessionManager.logOutUser()
        // Assert
        var observerdLiveData = authResourceLiveData.getValue(sessionManager.cachedUserLiveData)
        assertEquals(observerdLiveData?.status , AuthStatus.LOG_OUT)
    }

    @Test
    fun getAccessToken() {

        // Arrange
        var user = User("TOKEN" , LoginType.FROM_APP)
        var authResourceLiveData = LiveDataTestUtil<AuthResource<User>>()
        // Act
        sessionManager.setUser(user)
        // Assert
        var observerdLiveData = authResourceLiveData.getValue(sessionManager.cachedUserLiveData)
        assertEquals(observerdLiveData?.data?.token , "TOKEN" )
    }
}