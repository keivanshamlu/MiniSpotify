package com.example.minispotify.di.mainActivity.trackDetails

import androidx.lifecycle.ViewModel
import com.example.daggerkotlinn.di.ViewModelKey
import com.example.minispotify.viewModels.TrackDetailsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class TrackDetailsFragmentViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(TrackDetailsViewModel::class)
    abstract fun bindTrackDetailsFragmentViewModel(trackDetailsViewModel: TrackDetailsViewModel): ViewModel

}