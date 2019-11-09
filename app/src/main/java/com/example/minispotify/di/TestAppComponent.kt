package com.example.minispotify.di

import android.app.Application
import com.example.daggerkotlinn.di.ActivityBuildersModule
import com.example.daggerkotlinn.di.ViewModelFactoryModule
import com.example.minispotify.TestBaseApplication
import com.example.minispotify.managers.SessionManager
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


/**
 * this only come up when application is testing
 * only diffrence betweet this component and default
 * component is that this component has TestAppModule and
 * default component has AppModule
 */
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        TestAppModule::class,
        ActivityBuildersModule::class,
        ViewModelFactoryModule::class
    ]
)
interface TestAppComponent : AndroidInjector<TestBaseApplication> {

    // must add here b/c injecting into abstract class
    val sessionManager: SessionManager

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): TestAppComponent
    }
}