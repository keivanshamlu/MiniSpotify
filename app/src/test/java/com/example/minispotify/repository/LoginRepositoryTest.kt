package com.example.minispotify.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minispotify.managers.RequestManager
import com.example.minispotify.managers.SessionManager
import com.example.minispotify.model.LoginType
import com.example.minispotify.model.User
import com.example.minispotify.util.AuthResource
import com.example.minispotify.util.InstantExecutorExtension
import com.example.minispotify.util.LiveDataTestUtil
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


@ExtendWith(InstantExecutorExtension::class)
class LoginRepositoryTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    @Mock
    lateinit var sessionManager: SessionManager
    @Mock
    lateinit var requestManager: RequestManager

    lateinit var loginRepository: LoginRepository

    @BeforeEach
    fun initBeforeEach() {

        MockitoAnnotations.initMocks(this)

        loginRepository = LoginRepository(sessionManager , requestManager)
    }

    @Test
    fun setStateLoadingTest() {

        loginRepository.setStateLoading()
        verify(sessionManager).setStateLoading()
    }

    @Test
    fun setStateErrorTest() {

        loginRepository.setStateError(ArgumentMatchers.anyString())
        verify(sessionManager).setStateError(ArgumentMatchers.anyString())
    }

    @Test
    fun setUserErrorTest() {

        var user = User("123", LoginType.FROM_APP)
        loginRepository.setUser(user)
        verify(sessionManager).setUser(user)
    }


    @Test
    fun authUserLiveDataTest() {

        var authResource = AuthResource.authenticated(User("mona", LoginType.FROM_BROWSER))
        var authResourceLiveData =
            LiveDataTestUtil<AuthResource<User>>()

        loginRepository.cachedUserLiveData.value = authResource

        val observedData = authResourceLiveData.getValue(loginRepository.cachedUserLiveData)

        assertEquals(authResource, observedData)
    }
}
