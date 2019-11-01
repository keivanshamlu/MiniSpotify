package com.example.minispotify.di.mainActivity.login

import androidx.lifecycle.ViewModel
import com.example.daggerkotlinn.di.ViewModelKey
import com.example.minispotify.viewModels.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class LoginFragmentViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindAuthLoginFragmentViewModel(mainViewModel: LoginViewModel): ViewModel

}