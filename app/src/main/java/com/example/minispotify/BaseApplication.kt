package com.example.minispotify

import com.example.daggerkotlinn.di.DaggerAppComponent
import com.example.minispotify.util.TypefaceUtil
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class BaseApplication: DaggerApplication() {

    /**
     * setUp dagger here
     */
    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
        return DaggerAppComponent.builder()
            .application(this)
            .build()
        //return null
    }

    override fun onCreate() {
        super.onCreate()


        //set a font to whohe app using appTheme
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "Aller.ttf")
    }


}