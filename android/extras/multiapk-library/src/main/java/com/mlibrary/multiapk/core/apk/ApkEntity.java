package com.mlibrary.multiapk.core.apk;

import android.text.TextUtils;

import com.mlibrary.multiapk.base.runtime.RuntimeArgs;
import com.mlibrary.multiapk.base.util.FileUtil;
import com.mlibrary.multiapk.base.util.LogUtil;
import com.mlibrary.multiapk.core.hotpatch.Hotpatch;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class ApkEntity {
    private static final String TAG = ApkEntity.class.getName();
    private volatile boolean isBundleDexInstalled = false;
    public static final String SUFFIX = ApkManager.suffix_bundle_in_local;
    private File bundleFile = null;
    private File bundleDir = null;
    private String packageName = null;

    public ApkEntity(File bundleDir) throws Exception {
        this.bundleDir = bundleDir;
        if (bundleDir.exists()) {
            File[] localFiles = bundleDir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(SUFFIX);
                }
            });
            if (localFiles != null && localFiles.length > 0) {
                bundleFile = localFiles[0];
                packageName = bundleFile.getName().replaceAll(SUFFIX, "");
            }
        }
        if (bundleFile == null || !bundleFile.exists())
            throw new IllegalStateException("local bundle not exists! " + bundleDir);
        LogUtil.w(TAG, "bundleFilePath:" + bundleFile.getPath() + ", bundleFileName:" + bundleFile.getName() + ", packageName:" + packageName);
    }

    public ApkEntity(File bundleDir, String packageName, InputStream inputStream) throws Exception {
        if (bundleDir == null || TextUtils.isEmpty(packageName) || inputStream == null)
            throw new IllegalArgumentException("bundleDir:" + bundleDir + ", packageName:" + packageName + ", inputStream==null?" + (inputStream == null));

        this.bundleDir = bundleDir;
        if (!bundleDir.exists())
            bundleDir.mkdirs();
        this.packageName = packageName;
        this.bundleFile = new File(bundleDir, packageName + SUFFIX);

        try {
            FileUtil.copyInputStreamToFile(inputStream, this.bundleFile);
        } catch (Exception e) {
            FileUtil.deleteDirectory(bundleDir);
            throw new IOException("can not install bundle " + packageName, e);
        }
        LogUtil.w(TAG, "bundleFilePath:" + bundleFile.getPath() + ", bundleFileName:" + bundleFile.getName() + ", packageName:" + packageName);
    }

    public void installBundleDex() throws Exception {
        long startTime = System.currentTimeMillis();
        if (!isBundleDexInstalled) {
            //检测是否有热更新的合成包
            File syntheticBundleFile = Hotpatch.instance.getLatestSyntheticBundle(getPackageName());
            //File syntheticBundleFile = BundleHotPatch.getSyntheticBundle(getPackageName());
            if (syntheticBundleFile != null && syntheticBundleFile.exists()) {
                this.bundleDir = syntheticBundleFile.getParentFile();
                this.bundleFile = syntheticBundleFile;
                LogUtil.w(TAG, "发现合成包:" + bundleFile.getPath());
            }

            LogUtil.w(TAG, "classloader:" + RuntimeArgs.androidApplication.getClassLoader().toString());
            LogUtil.w(TAG, "bundleDir:" + bundleDir.getPath());
            LogUtil.w(TAG, "bundleFile:" + bundleFile.getPath());
            LogUtil.w(TAG, "isHotFix:" + false);

            List<File> bundleList = new ArrayList<>();
            bundleList.add(this.bundleFile);
            LogUtil.w(TAG, "bundleList:" + Arrays.toString(bundleList.toArray()));
            ApkDexInstaller.installBundleDexs(RuntimeArgs.androidApplication.getClassLoader(), bundleDir, bundleList, false);
            //ApkDexInstaller.installBundleDex(RuntimeArgs.androidApplication.getClassLoader(), bundleList, bundleDir, false);
            isBundleDexInstalled = true;
        }
        LogUtil.v(TAG, "installBundleDex：" + getPackageName() + ", 耗时: " + String.valueOf(System.currentTimeMillis() - startTime) + "ms");
    }

    public void delete() throws Exception {
        FileUtil.deleteDirectory(bundleDir);
    }

    public boolean isBundleDexInstalled() {
        return this.isBundleDexInstalled;
    }

    public String getBundleFilePath() {
        return this.bundleFile.getPath();
    }

    public String getPackageName() {
        LogUtil.d(TAG, "getPackageName:" + packageName);
        return this.packageName;
    }

    public String toString() {
        return "\nbundle: " + bundleDir + File.separator + packageName + SUFFIX;
    }
}
