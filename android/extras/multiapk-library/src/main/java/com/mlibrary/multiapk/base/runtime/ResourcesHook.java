package com.mlibrary.multiapk.base.runtime;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.mlibrary.multiapk.base.hack.AndroidHack;
import com.mlibrary.multiapk.base.hack.SysHacks;
import com.mlibrary.multiapk.base.util.LogUtil;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yb.wang on 15/1/5.
 * 挂载载系统资源中，处理框架资源加载
 */
public class ResourcesHook extends Resources {
    public static final String TAG = ResourcesHook.class.getName();

    @SuppressWarnings("deprecation")
    private ResourcesHook(AssetManager assets, Resources resources) {
        super(assets, resources.getDisplayMetrics(), resources.getConfiguration());
    }

    public static void newResourcesHook(Application application, Resources resources, List<String> assetPathList) throws Exception {
        LogUtil.d(TAG, "...............................");
        LogUtil.d(TAG, "assetPathList:" + (assetPathList == null ? "null" : Arrays.toString(assetPathList.toArray())));
        if (assetPathList != null && !assetPathList.isEmpty()) {
            Resources delegateResources;
            assetPathList.add(0, application.getApplicationInfo().sourceDir);

            AssetManager assetManager = AssetManager.class.newInstance();
            for (String assetPath : assetPathList)
                SysHacks.AssetManager_addAssetPath.invoke(assetManager, assetPath);//addAssetPath

            //处理小米UI资源
            if (resources == null || !resources.getClass().getName().equals("android.content.res.MiuiResources")) {
                delegateResources = new ResourcesHook(assetManager, resources);
            } else {
                Constructor declaredConstructor = Class.forName("android.content.res.MiuiResources").getDeclaredConstructor(AssetManager.class, DisplayMetrics.class, Configuration.class);
                declaredConstructor.setAccessible(true);
                delegateResources = (Resources) declaredConstructor.newInstance(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
            }

            RuntimeArgs.delegateResources = delegateResources;
            AndroidHack.injectResources(application, delegateResources);
        }
        LogUtil.d(TAG, "...............................");
    }
}
