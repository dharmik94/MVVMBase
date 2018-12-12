package com.dharmik.mvvmarch.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.dharmik.mvvmarch.base.BaseApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(var app: BaseApp) {

    @Provides
    @Singleton
    fun provideApp(): Application = app


    @Provides
    @Singleton
    fun provideContext(): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(app)
}