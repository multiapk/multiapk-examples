package org.smartrobot.base

import android.app.Application
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatDelegate
import org.smartrobot.BuildConfig
import org.smartrobot.multiapk.MultiApk
import org.smartrobot.util.DefaultLogUtil

open class DefaultBaseApplication : Application() {

    companion object {
        lateinit var instance: DefaultBaseApplication
    }

    override fun onCreate() {
        if (!BuildConfig.solidMode)
            MultiDex.install(this)
        else
            MultiApk.init(this)
        super.onCreate()
        instance = this
        DefaultLogUtil.setDebugAble(BuildConfig.DEBUG)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//selector vector support
        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in MActivityLifecycleCallbacks
        // with just a few lines of code. Now *that's* nice.
        registerActivityLifecycleCallbacks(DefaultActivityLifecycleCallbacks())
        DefaultLogUtil.setDebugAble(BuildConfig.solidMode)
    }
}