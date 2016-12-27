package com.mlibrary.patch.framework;

import android.os.Build;

import com.mlibrary.patch.log.Logger;
import com.mlibrary.patch.log.LoggerFactory;
import com.mlibrary.patch.runtime.RuntimeArgs;
import com.mlibrary.patch.util.FileUtil;
import com.mlibrary.patch.util.MLibraryPatchUtil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Created by yb.wang on 14/12/31.
 * 框架包含自身SystemBundle
 * 1.管理各个Bundle的启动，更新，卸载
 * 2.提供框架启动Runtime
 */
public final class Framework {
    public static final String SYMBOL_SEMICOLON = ";";
    private static final Logger log = LoggerFactory.getLogcatLogger(MLibraryPatchUtil.TAG + ":Framework");
    static final Map<String, Bundle> bundles = new ConcurrentHashMap<>();
    private static String storageLocation;
    private static long nextBundleID = 1;

    private Framework() {
    }

    static void startup(boolean needReInitBundle) throws BundleException {
        log.log("*------------------------------------*", Logger.LogLevel.DEBUG);
        //noinspection deprecation
        log.log(" Ctrip Bundle on " + Build.MODEL + "|starting...", Logger.LogLevel.DEBUG);
        log.log("*------------------------------------*", Logger.LogLevel.DEBUG);

        long currentTimeMillis = System.currentTimeMillis();
        String baseDir = null;
        //String baseDir = RuntimeArgs.androidApplication.getFilesDir().getAbsolutePath();
        File externalFile = RuntimeArgs.androidApplication.getExternalFilesDir(null);
        if (externalFile != null)
            baseDir = externalFile.getAbsolutePath();
        if (baseDir == null)
            baseDir = RuntimeArgs.androidApplication.getFilesDir().getAbsolutePath();
        storageLocation = baseDir + File.separatorChar + "storage" + File.separatorChar;
        log.w("storageLocation:" + storageLocation);
        if (needReInitBundle) {
            log.w("重新初始化,即将删除:" + storageLocation);
            File file = new File(storageLocation);
            if (file.exists())
                FileUtil.deleteDirectory(file);
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
            storeProfile();
        } else {
            restoreProfile();
        }
        log.log("*------------------------------------*", Logger.LogLevel.DEBUG);
        log.log(" Framework " + (needReInitBundle ? "restarted" : "start") + " in " + (System.currentTimeMillis() - currentTimeMillis) + " ms", Logger.LogLevel.DEBUG);
        log.log("*------------------------------------*", Logger.LogLevel.DEBUG);
    }

    public static List<Bundle> getBundles() {
        List<Bundle> arrayList = new ArrayList<>(bundles.size());
        synchronized (bundles) {
            arrayList.addAll(bundles.values());
        }
        return arrayList;
    }

    static Bundle getBundle(String str) {
        return bundles.get(str);
    }

    public static Bundle getBundle(long id) {
        synchronized (bundles) {
            for (Bundle bundle : bundles.values())
                if (bundle.getBundleId() == id)
                    return bundle;
            return null;
        }
    }

    private static void storeProfile() {
        log.i("保存 profile");
        //noinspection SuspiciousToArrayCall
        BundleImpl[] bundleImplArr = getBundles().toArray(new BundleImpl[bundles.size()]);
        for (BundleImpl bundleImpl : bundleImplArr)
            bundleImpl.updateMetadata();
        storeMetadata();
    }

    private static void storeMetadata() {
        log.i("保存到 metadata");
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(new File(storageLocation, "meta")));
            dataOutputStream.writeLong(nextBundleID);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (Throwable e) {
            log.log("Could not save meta data.", Logger.LogLevel.ERROR, e);
        }
    }

    private static int restoreProfile() {
        log.i("还原 profile");
        try {
            File file = new File(storageLocation, "meta");
            if (file.exists()) {
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
                nextBundleID = dataInputStream.readLong();
                dataInputStream.close();
                File file2 = new File(storageLocation);
                File[] listFiles = file2.listFiles();
                int i = 0;
                while (i < listFiles.length) {
                    if (listFiles[i].isDirectory() && new File(listFiles[i], "meta").exists()) {
                        try {
                            String location = new BundleImpl(listFiles[i]).getLocation();
                            log.log("RESTORED BUNDLE " + location, Logger.LogLevel.DEBUG);
                        } catch (Exception e) {
                            log.log(e.getMessage(), Logger.LogLevel.ERROR, e.getCause());
                        }
                    }
                    i++;
                }
                return 1;
            }
            log.log("Profile not found, performing clean start ...", Logger.LogLevel.DEBUG);
            return -1;
        } catch (Exception e2) {
            e2.printStackTrace();
            return 0;
        }
    }

    static BundleImpl installNewBundle(String location, InputStream inputStream) throws BundleException {
        log.d("安装新的bundle:" + location);
        BundleImpl bundleImpl = (BundleImpl) getBundle(location);
        if (bundleImpl != null)
            return bundleImpl;
        long j = nextBundleID;
        nextBundleID = 1 + j;
        bundleImpl = new BundleImpl(new File(storageLocation, String.valueOf(j)), location, j, inputStream);
        storeMetadata();
        return bundleImpl;
    }
}