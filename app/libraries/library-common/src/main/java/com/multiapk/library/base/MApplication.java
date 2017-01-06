package com.multiapk.library.base;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.mlibrary.patch.MDynamicLib;
import com.multiapk.library.BuildConfig;

public class MApplication extends Application {
    @Override
    public void onCreate() {
        if (!BuildConfig.solidMode)
            MultiDex.install(this);
        else
            MDynamicLib.init(this);
        super.onCreate();
    }
}
