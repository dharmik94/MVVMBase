package com.dharmik.mvvmarch.di.component

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.SharedPreferences
import com.dharmik.mvvmarch.base.BaseApp
import com.dharmik.mvvmarch.di.module.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface ApplicationComponent {

    fun inject(app: BaseApp)

    fun app(): Application

    fun context(): Context

    fun preferences(): SharedPreferences

}