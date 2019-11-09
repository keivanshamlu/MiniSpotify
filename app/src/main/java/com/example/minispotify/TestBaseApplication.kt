package com.example.minispotify

import com.example.minispotify.di.DaggerTestAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

/**
 * this runs only when application is testing
 * it will come up the TestAppComponent
 */
class TestBaseApplication: DaggerApplication() {

    /**
     * setUp dagger here
     */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
        return DaggerTestAppComponent.builder()
            .application(this)
            .build()
        //return null
    }

}
