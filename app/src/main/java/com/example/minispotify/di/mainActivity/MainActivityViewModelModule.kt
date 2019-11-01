package com.example.minispotify.di.mainActivity

import androidx.lifecycle.ViewModel
import com.example.daggerkotlinn.di.ViewModelKey
import com.example.minispotify.viewModels.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class MainActivityViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindAuthMainActivityViewModel(mainViewModel: MainActivityViewModel): ViewModel



}