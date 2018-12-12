package com.dharmik.mvvmarch.base

import android.app.Application
import com.dharmik.mvvmarch.di.component.ApplicationComponent
import com.dharmik.mvvmarch.di.component.DaggerApplicationComponent
import com.dharmik.mvvmarch.di.module.AppModule

class BaseApp : Application() {
    companion object {
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.builder()
            .appModule(AppModule(this))
            .build()
        component.inject(this)
    }
}