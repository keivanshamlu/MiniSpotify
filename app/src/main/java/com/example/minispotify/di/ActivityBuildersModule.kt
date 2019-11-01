package com.example.daggerkotlinn.di




import com.example.minispotify.di.mainActivity.MainActivityFragmentBuildersModule
import com.example.minispotify.di.mainActivity.MainActivityViewModelModule
import com.example.minispotify.ui.activities.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * in this class we assign modules to MainActivity
 * we use ContributesAndroidInjector so this modules
 * provide objects in mainActivity lifeTime
 * MainActivityViewModelModule provides injection for mainActivityViewModel
 * MainActivityFragmentBuildersModule is a builder module
 * so we can have subcommponents for each fragment
 */
@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
        modules = [ MainActivityFragmentBuildersModule::class , MainActivityViewModelModule::class]
    )
    abstract fun contributeAuthActivity(): MainActivity

}