package com.multiapk

import com.squareup.leakcanary.LeakCanary

import org.smartrobot.base.DefaultBaseApplication

class BeautyApplication : DefaultBaseApplication() {
    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
    }
}
