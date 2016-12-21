package com.mlibrary.patch.runtime;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.mlibrary.patch.framework.Bundle;
import com.mlibrary.patch.framework.BundleImpl;
import com.mlibrary.patch.framework.Framework;
import com.mlibrary.patch.hack.AndroidHack;
import com.mlibrary.patch.hack.SysHacks;
import com.mlibrary.patch.log.Logger;
import com.mlibrary.patch.log.LoggerFactory;
import com.mlibrary.patch.util.MLibraryPatchUtil;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by yb.wang on 15/1/5.
 * 挂载载系统资源中，处理框架资源加载
 */
public class DelegateResources extends Resources {
    private static final Logger log = LoggerFactory.getLogcatLogger(MLibraryPatchUtil.TAG + ":DelegateResources");

    @SuppressWarnings("deprecation")
    private DelegateResources(AssetManager assets, Resources resources) {
        super(assets, resources.getDisplayMetrics(), resources.getConfiguration());
    }

    public static void newDelegateResources(Application application, Resources resources) throws Exception {
        List<Bundle> bundles = Framework.getBundles();
        if (bundles != null && !bundles.isEmpty()) {
            Resources delegateResources;
            List<String> arrayList = new ArrayList<>();
            arrayList.add(application.getApplicationInfo().sourceDir);
            for (Bundle bundle : bundles)
                arrayList.add(((BundleImpl) bundle).getArchive().getArchiveFile().getAbsolutePath());
            AssetManager assetManager = AssetManager.class.newInstance();
            for (String str : arrayList)
                SysHacks.AssetManager_addAssetPath.invoke(assetManager, str);
            //处理小米UI资源
            if (resources == null || !resources.getClass().getName().equals("android.content.res.MiuiResources")) {
                delegateResources = new DelegateResources(assetManager, resources);
            } else {
                Constructor declaredConstructor = Class.forName("android.content.res.MiuiResources").getDeclaredConstructor(AssetManager.class, DisplayMetrics.class, Configuration.class);
                declaredConstructor.setAccessible(true);
                delegateResources = (Resources) declaredConstructor.newInstance(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
            }
            RuntimeArgs.delegateResources = delegateResources;
            AndroidHack.injectResources(application, delegateResources);
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append("newDelegateResources [");
            for (int i = 0; i < arrayList.size(); i++) {
                if (i > 0)
                    stringBuffer.append(",");
                stringBuffer.append(arrayList.get(i));
            }
            stringBuffer.append("]");
            log.log(stringBuffer.toString(), Logger.LogLevel.DEBUG);
        }
    }
}
