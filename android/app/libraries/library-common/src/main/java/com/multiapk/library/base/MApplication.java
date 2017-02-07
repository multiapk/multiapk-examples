package com.multiapk.library.base;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.mlibrary.multiapk.MultiApk;
import com.multiapk.library.BuildConfig;

public class MApplication extends Application {
    @Override
    public void onCreate() {
        if (!BuildConfig.solidMode)
            MultiDex.install(this);
        else
            MultiApk.init(this);
        super.onCreate();
    }
}
