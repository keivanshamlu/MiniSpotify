package com.example.minispotify.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minispotify.model.LoginType
import com.example.minispotify.model.User
import com.example.minispotify.repository.LoginRepository
import com.example.minispotify.util.AuthResource
import com.example.minispotify.util.InstantExecutorExtension
import com.example.minispotify.util.LiveDataTestUtil
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExtendWith(InstantExecutorExtension::class)
class LoginViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    lateinit var loginViewModel: LoginViewModel

    @Mock
    lateinit var loginRepository: LoginRepository


    @BeforeEach
    fun initBeforeEach() {

        MockitoAnnotations.initMocks(this)

        loginViewModel = LoginViewModel(loginRepository)
    }

    @Test
    fun getCachedUserLiveData() {


        // Arrange
        var user = User("token" , LoginType.FROM_APP)
        var authResource = AuthResource.authenticated(user)
        var resourceLiveData = LiveDataTestUtil<AuthResource<User>>()

        // Act
        loginViewModel.cachedUserLiveData.value = authResource

        // Assert
        val userLiveDataValue = resourceLiveData.getValue(loginViewModel.cachedUserLiveData)
        assertEquals(userLiveDataValue , authResource)
    }

    @Test
    fun setStateLoading() {

        loginViewModel.setStateLoading()

        verify(loginRepository).setStateLoading()
    }

    @Test
    fun setStateError() {

        // Arrange
        var errorMessage = "error1"
        // Act
        loginViewModel.setStateError(errorMessage)
        // Assert
        verify(loginRepository).setStateError(errorMessage)
    }

    @Test
    fun setUser() {

        // Arrange
        var user = User("token" , LoginType.FROM_APP)
        // Act
        loginViewModel.setUser(user)
        // Assert
        verify(loginRepository).setUser(user)
    }


}