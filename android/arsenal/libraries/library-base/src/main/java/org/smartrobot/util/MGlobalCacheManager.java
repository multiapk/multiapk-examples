package org.smartrobot.util;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.smartrobot.base.DefaultBaseApplication;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理应用程序全局的 entity cache
 */
@SuppressWarnings("ResultOfMethodCallIgnored,unused")
public class MGlobalCacheManager {

    public interface OnCacheCallBack<T> {
        void onSuccess(T successObject);

        void onFailure(T failureObject);
    }


    private MGlobalCacheManager() {
    }

    private static class SingleTon {
        public static MGlobalCacheManager instance = new MGlobalCacheManager();
    }

    public static MGlobalCacheManager getInstance() {
        return SingleTon.instance;
    }

    private ConcurrentHashMap<String, ConcurrentHashMap<String, Object>> allModuleCacheMap = new ConcurrentHashMap<>();

    public void put(@NonNull String module, @NonNull String key, @NonNull Object value) {
        if (TextUtils.isEmpty(module) || TextUtils.isEmpty(key)) {
            return;
        }
        ConcurrentHashMap<String, Object> subModuleCacheMap = null;
        if (allModuleCacheMap.containsKey(module)) {
            subModuleCacheMap = allModuleCacheMap.get(module);
        }

        if (subModuleCacheMap == null)
            subModuleCacheMap = new ConcurrentHashMap<>();

        subModuleCacheMap.put(key, value);
        allModuleCacheMap.put(module, subModuleCacheMap);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(@NonNull String module, @NonNull String key) {
        if (!TextUtils.isEmpty(module) && !TextUtils.isEmpty(key)) {
            ConcurrentHashMap<String, Object> subModuleCacheMap = null;
            if (allModuleCacheMap.containsKey(module))
                subModuleCacheMap = allModuleCacheMap.get(module);

            if (subModuleCacheMap != null) {
                try {
                    return (T) subModuleCacheMap.get(key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void clean(@NonNull String module) {
        if (!TextUtils.isEmpty(module))
            allModuleCacheMap.remove(module);
    }

    public static File getPackageDir() {
        File cacheDir = null;
        if (MSdCardUtil.isSdCardExist()) {
            // 荣耀6 会有很多警告
            // logDir = MApplication.getInstance().getExternalFilesDir("cache");
            cacheDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + DefaultBaseApplication.INSTANCE.getPackageName());
        } else {
            cacheDir = new File(DefaultBaseApplication.INSTANCE.getFilesDir().getAbsolutePath());
        }

        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir;
    }

    public static File getCacheDir() {
        File cacheDir = new File(getPackageDir(), "cache");
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir;
    }

    public static File getChildCacheDir(String childDir) {
        if (TextUtils.isEmpty(childDir))
            return getCacheDir();
        File cacheDir = new File(getCacheDir(), childDir);
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir;
    }
}
