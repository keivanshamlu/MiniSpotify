package com.example.minispotify.di.mainActivity.search

import androidx.lifecycle.ViewModel
import com.example.daggerkotlinn.di.ViewModelKey
import com.example.minispotify.viewModels.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SearchFragmentViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchFragmentViewModel(searchViewModel: SearchViewModel): ViewModel

}