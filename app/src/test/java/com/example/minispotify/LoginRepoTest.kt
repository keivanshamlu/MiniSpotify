package com.example.minispotify

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.minispotify.managers.RequestManager
import com.example.minispotify.managers.SessionManager
import com.example.minispotify.model.LoginType
import com.example.minispotify.model.User
import com.example.minispotify.repository.LoginRepository
import com.example.minispotify.util.AuthResource
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class LoginRepoTest {

    @get:Rule
    val testRule = InstantTaskExecutorRule()

    @Mock
    lateinit var sessionManager: SessionManager

    @Mock
    lateinit var requestManager: RequestManager
    lateinit var loginRepository: LoginRepository

    @Before
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



        // Attacch fake observer
        val observer = mock(Observer::class.java) as Observer<AuthResource<User>>
        loginRepository.cachedUserLiveData.observeForever(observer)

        loginRepository.cachedUserLiveData.value = authResource


    }
}
