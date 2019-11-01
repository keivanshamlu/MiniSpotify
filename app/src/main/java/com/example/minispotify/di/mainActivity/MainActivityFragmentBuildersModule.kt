package com.example.minispotify.di.mainActivity


import com.example.minispotify.di.mainActivity.login.LoginFragmentModule
import com.example.minispotify.di.mainActivity.login.LoginFragmentViewModelsModule
import com.example.minispotify.di.mainActivity.search.SearchFragmentModule
import com.example.minispotify.di.mainActivity.search.SearchFragmentViewModelsModule
import com.example.minispotify.di.mainActivity.trackDetails.TrackDetailsFragmentModule
import com.example.minispotify.di.mainActivity.trackDetails.TrackDetailsFragmentViewModelsModule
import com.example.minispotify.ui.fragments.LoginFragment
import com.example.minispotify.ui.fragments.SearchFragment
import com.example.minispotify.ui.fragments.TrackDetailsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * attach each fragment modules to lifecycle of that particular fragment
 */
@Module
abstract class MainActivityFragmentBuildersModule{

    @ContributesAndroidInjector(modules = [LoginFragmentModule::class , LoginFragmentViewModelsModule::class])
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector(modules = [SearchFragmentModule::class , SearchFragmentViewModelsModule::class])
    abstract fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector(modules = [TrackDetailsFragmentModule::class , TrackDetailsFragmentViewModelsModule::class])
    abstract fun contributeTrackDetailsFragment(): TrackDetailsFragment

}