package com.mlibrary.multiapk;

import android.app.Application;

import com.mlibrary.multiapk.core.apk.ApkManager;

import java.io.File;

public class MultiApk {
    public static String defaultActivityWhileClassNotFound = null;

    public static void init(Application application) {
        init(application, null, true);
    }

    public static void init(final Application application, String defaultActivityWhileClassNotFound, boolean isOpenLog) {
        MultiApk.defaultActivityWhileClassNotFound = defaultActivityWhileClassNotFound;
        ApkManager.instance.init(application, isOpenLog);
    }

    public static File getBaseDir(Application androidApplication) {
//        String baseDir = androidApplication.getFilesDir().getAbsolutePath();
        //为了方便调试，暂时优先放到SDCard
        File baseDir = androidApplication.getExternalFilesDir(null);
        if (baseDir == null)
            baseDir = androidApplication.getFilesDir();
        return baseDir;
//        return new File(baseDir);
    }
}