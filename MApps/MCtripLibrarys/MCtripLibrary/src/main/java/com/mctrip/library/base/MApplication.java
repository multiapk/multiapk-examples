package com.mctrip.library.base;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.mctrip.library.BuildConfig;
import com.mlibrary.patch.util.MLibraryPatchUtil;

public class MApplication extends Application {
    @Override
    public void onCreate() {
        if (!BuildConfig.solidMode)
            MultiDex.install(this);
        super.onCreate();
        if (BuildConfig.solidMode)
            MLibraryPatchUtil.init(this);
    }
}