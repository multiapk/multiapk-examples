package com.multiapk.library.base

import android.app.Application
import android.support.multidex.MultiDex
import com.mlibrary.multiapk.MultiApk
import com.multiapk.library.BuildConfig

open class CMApplication : Application() {
    override fun onCreate() {
        if (!BuildConfig.solidMode)
            MultiDex.install(this)
        else
            MultiApk.init(this)
        super.onCreate()
    }
}

