package com.mlibrary.multiapk.core.apk;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.mlibrary.multiapk.MultiApk;
import com.mlibrary.multiapk.base.hack.AndroidHack;
import com.mlibrary.multiapk.base.hack.SysHacks;
import com.mlibrary.multiapk.base.runtime.InstrumentationHook;
import com.mlibrary.multiapk.base.runtime.ResourcesHook;
import com.mlibrary.multiapk.base.runtime.RuntimeArgs;
import com.mlibrary.multiapk.base.util.FileUtil;
import com.mlibrary.multiapk.base.util.LogUtil;
import com.mlibrary.multiapk.base.util.PreferencesUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@SuppressWarnings("ResultOfMethodCallIgnored")
public enum ApkManager {
    instance;

    private static final String TAG = ApkManager.class.getName();
    public static final String bundleLibPath = "assets/baseres/";
    public static final String suffix_bundle_in_local = ".zip";//if (name.endsWith(APK_SUFFIX) || name.endsWith(JAR_SUFFIX) || name.endsWith(ZIP_SUFFIX))
    public static final String suffix_bundle_in_assets = ".zip";
    public static final String suffix_dex = ".dex";

    private final Map<String, ApkEntity> bundles = new ConcurrentHashMap<>();
    private File bundlesDir;

    public void init(Application application, boolean isOpenLog) {
        long startTime = System.currentTimeMillis();
        LogUtil.setDebugAble(isOpenLog);
        LogUtil.w(TAG, "======================================================================");
        LogUtil.w(TAG, "******** mdynamiclib init start **************************************");
        LogUtil.w(TAG, "======================================================================");
        try {
            SysHacks.defineAndVerify();
            RuntimeArgs.androidApplication = application;
            RuntimeArgs.delegateResources = application.getResources();
            AndroidHack.injectInstrumentationHook(new InstrumentationHook(AndroidHack.getInstrumentation(), application.getBaseContext()));

            bundlesDir = new File(MultiApk.getBaseDir(application), "bundles");
            LogUtil.w(TAG, "bundle location:" + bundlesDir);

            checkStatus(application);
        } catch (Exception e) {
            LogUtil.e(TAG, "******** mdynamiclib init failure ************************************", e);
        } finally {
            LogUtil.w(TAG, "======================================================================");
            LogUtil.w(TAG, "******** mdynamiclib init end [耗时: " + (System.currentTimeMillis() - startTime) + "ms]");
            LogUtil.w(TAG, "======================================================================");
        }
    }

    private void checkStatus(final Application application) {
        LogUtil.e(TAG, "checkStatus: start :check is need reCopyInstall bundles");
        LogUtil.e(TAG, ">>>>-------------------------------------------------------------->>>>");
        final long startTime = System.currentTimeMillis();
        boolean isLocalBundlesValid = isLocalBundlesValid();
        if (!TextUtils.equals(getCurrentBundleKey(application), getLastBundleKey(application)) || !isLocalBundlesValid) {
            LogUtil.d(TAG, "checkStatus: currentBundleKey != lastBundleKey , delete local and reCopyInstall bundles, isLocalBundlesValid==" + isLocalBundlesValid);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    LogUtil.w(TAG, "checkStatus: start new thread to reCopyInstall bundles");
                    deleteAllBundlesIfExists();
                    copyBundlesToLocalFromAssets(application);
                    installBundleDexs();
                    LogUtil.e(TAG, "checkStatus: end : 耗时: " + (System.currentTimeMillis() - startTime) + "ms");
                    LogUtil.e(TAG, "<<<<--------------------------------------------------------------<<<<");
                }
            }).start();
        } else {
            LogUtil.d(TAG, "checkStatus: currentBundleKey == lastBundleKey , restore from local hotfix and bundles, isLocalBundlesValid==true");
            loadBundlesFromLocal();
            installBundleDexs();
            LogUtil.e(TAG, "checkStatus: end : 耗时: " + (System.currentTimeMillis() - startTime) + "ms");
            LogUtil.e(TAG, "<<<<--------------------------------------------------------------<<<<");
        }
    }

    private boolean isLocalBundlesValid() {
        //校验local所有数据正确性，如果不正确 deleteAllBundlesIfExists，重新 copyToLocal
        //验证bundles md5，最好在md5正确的bundle.zip里重新释放bundle.dex,确保万无一失，防止被恶意修改
        return true;
    }

    public void deleteBundleIfExists(String packageName) throws Exception {
        ApkEntity apkEntity = bundles.get(packageName);
        if (apkEntity != null)
            apkEntity.delete();
    }

    public void deleteAllBundlesIfExists() {
        LogUtil.w(TAG, "deleteAllBundlesIfExists start:" + bundlesDir);
        long startTime = System.currentTimeMillis();
        if (bundlesDir.exists())
            FileUtil.deleteDirectory(bundlesDir);
        bundlesDir.mkdirs();
        LogUtil.w(TAG, "deleteAllBundlesIfExists end 总耗时: " + String.valueOf(System.currentTimeMillis() - startTime) + "ms");
    }

    public void installBundleDexs() {
        LogUtil.w(TAG, "installBundleDexs：start");
        long startTime = System.currentTimeMillis();
        for (ApkEntity apkEntity : getBundles()) {
            try {
                apkEntity.installBundleDex();
            } catch (Exception e) {
                LogUtil.e(TAG, "installBundleDex exception", e);
            }
        }
        try {
            List<String> assetsPathList = new ArrayList<>();
            for (ApkEntity apkEntity : getBundles())
                assetsPathList.add((apkEntity).getBundleFilePath());
            ResourcesHook.newResourcesHook(RuntimeArgs.androidApplication, RuntimeArgs.delegateResources, assetsPathList);
        } catch (Exception e) {
            LogUtil.e(TAG, "DelegateResources.newResourcesHook exception", e);
        }
        LogUtil.w(TAG, "installBundleDexs：end 总耗时: " + String.valueOf(System.currentTimeMillis() - startTime) + "ms");
    }

    public static List<String> getPathListByFilter(ZipFile zipFile, String prefix, String suffix) {
        List<String> arrayList = new ArrayList<>();
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            String name = ((ZipEntry) entries.nextElement()).getName();
            if (!TextUtils.isEmpty(name) && name.startsWith(prefix) && name.endsWith(suffix))
                arrayList.add(name);
        }
        return arrayList;
    }

    public File getBaseBundleFile(String packageName) {
        return new File(bundlesDir, packageName + File.separator + packageName + suffix_bundle_in_assets);
    }

    public void copyBundlesToLocalFromAssets(Application application) {
        LogUtil.w(TAG, "start:allBundles:\n" + getBundles().toString());
        long startTime = System.currentTimeMillis();
        ZipFile zipFile = null;
        try {
            LogUtil.d(TAG, "open zip sourceDir: " + application.getApplicationInfo().sourceDir);
            zipFile = new ZipFile(application.getApplicationInfo().sourceDir);
            for (String bundleBasePath : getPathListByFilter(zipFile, bundleLibPath, suffix_bundle_in_assets)) {
                String packageName = bundleBasePath.substring(bundleBasePath.indexOf(bundleLibPath) + bundleLibPath.length(), bundleBasePath.indexOf(suffix_bundle_in_assets)).replace("_", ".");
                LogUtil.d(TAG, "bundleBasePath:" + bundleBasePath + ", packageName:" + packageName);
                if (!isLocalBundleExists(packageName)) {
                    try {
                        ApkEntity apkEntity = bundles.get(packageName);
                        if (apkEntity == null) {
                            //临时修改本地存储路径名 //todo
                            apkEntity = new ApkEntity(new File(bundlesDir, packageName), packageName, zipFile.getInputStream(zipFile.getEntry(bundleBasePath)));
                            bundles.put(apkEntity.getPackageName(), apkEntity);
                        }
                    } catch (Exception e) {
                        LogUtil.e(TAG, "new Bundle failure: " + packageName, e);
                    }
                }
            }
            saveBundleKey(application, getCurrentBundleKey(application));
        } catch (Exception e) {
            LogUtil.e(TAG, "open zip sourceDir error! " + application.getApplicationInfo().sourceDir, e);
        } finally {
            try {
                if (zipFile != null)
                    zipFile.close();
            } catch (Exception ignore) {
            }
        }
        LogUtil.w(TAG, "end:allBundles:耗时: " + (System.currentTimeMillis() - startTime) + "ms \n" + getBundles().toString());
    }

    private boolean isLocalBundleExists(String packageName) {
        boolean isLocalBundleExists = bundles.get(packageName) != null;
        LogUtil.d(TAG, "isLocalBundleExists: " + isLocalBundleExists + ", packageName:" + packageName);
        return isLocalBundleExists;
    }

    public List<ApkEntity> getBundles() {
        List<ApkEntity> arrayList = new ArrayList<>(bundles.size());
        synchronized (bundles) {
            arrayList.addAll(bundles.values());
        }
        return arrayList;
    }

    private int loadBundlesFromLocal() {
        LogUtil.w(TAG, "start");
        try {
            File[] bundleDirs = bundlesDir.listFiles();
            if (bundleDirs != null) {
                for (File bundleDir : bundleDirs) {
                    try {
                        ApkEntity apkEntity = new ApkEntity(bundleDir);
                        bundles.put(apkEntity.getPackageName(), apkEntity);
                        LogUtil.v(TAG, "success to load bundle: " + apkEntity.getBundleFilePath());
                    } catch (Exception e) {
                        LogUtil.e(TAG, "failure to load bundle: " + bundleDir, e);
                    }
                }
            }
            LogUtil.w(TAG, "end , return 1(成功)");
            return 1;
        } catch (Exception e) {
            LogUtil.e(TAG, "end , return 0(异常)", e);
            return 0;
        }
    }

    public static String getCurrentBundleKey(Application application) {
        String bundleKey = null;
        try {
            PackageInfo packageInfo = application.getPackageManager().getPackageInfo(application.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            bundleKey = "app.version." + String.valueOf(packageInfo.versionCode) + "." + packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtil.w(TAG, "[get] currentBundleKey==" + bundleKey);
        return bundleKey;
    }

    private static final String KEY_LAST_BUNDLE = "KEY_LAST_BUNDLE_KEY";

    public static void saveBundleKey(Application application, String bundleKey) {
        LogUtil.w(TAG, "saveBundleKey:" + bundleKey);
        PreferencesUtil.getInstance(application).putString(KEY_LAST_BUNDLE, bundleKey);
    }

    public static String getLastBundleKey(Application application) {
        String lastBundleKey = PreferencesUtil.getInstance(application).getString(KEY_LAST_BUNDLE);
        LogUtil.w(TAG, "[get] lastBundleKey==" + lastBundleKey);
        return lastBundleKey;
    }

}
