package org.smartrobot.util

import android.os.Environment
import android.text.TextUtils
import org.smartrobot.base.DefaultBaseApplication
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * 管理应用程序全局的 entity cache
 */
class DefaultCacheManager private constructor() {

    interface OnCacheCallBack<T> {
        fun onSuccess(successObject: T?)

        fun onFailure(failureObject: T?)
    }

    private object SingleTon {
        var instance = DefaultCacheManager()
    }

    private val allModuleCacheMap = ConcurrentHashMap<String, ConcurrentHashMap<String, Any>>()

    fun put(module: String, key: String, value: Any) {
        if (TextUtils.isEmpty(module) || TextUtils.isEmpty(key)) {
            return
        }
        var subModuleCacheMap: ConcurrentHashMap<String, Any>? = null
        if (allModuleCacheMap.containsKey(module)) {
            subModuleCacheMap = allModuleCacheMap[module]
        }

        if (subModuleCacheMap == null)
            subModuleCacheMap = ConcurrentHashMap<String, Any>()

        subModuleCacheMap.put(key, value)
        allModuleCacheMap.put(module, subModuleCacheMap)
    }

    operator fun <T> get(module: String, key: String): T? {
        if (!TextUtils.isEmpty(module) && !TextUtils.isEmpty(key)) {
            var subModuleCacheMap: ConcurrentHashMap<String, Any>? = null
            if (allModuleCacheMap.containsKey(module))
                subModuleCacheMap = allModuleCacheMap[module]

            if (subModuleCacheMap != null) {
                try {
                    return subModuleCacheMap[key] as T
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
        return null
    }

    fun clean(module: String) {
        if (!TextUtils.isEmpty(module))
            allModuleCacheMap.remove(module)
    }

    companion object {

        val instance: DefaultCacheManager
            get() = SingleTon.instance

        // 荣耀6 会有很多警告
        // logDir = DefaultBaseApplication.INSTANCE.getExternalFilesDir("cache");
        val packageDir: File
            get() {
                var cacheDir: File? = null
                if (DefaultSystemUtil.isSdCardExist) {
                    cacheDir = File(Environment.getExternalStorageDirectory().absolutePath + "/Android/data/" + DefaultBaseApplication.Companion.INSTANCE.packageName)
                } else {
                    cacheDir = File(DefaultBaseApplication.Companion.INSTANCE.filesDir.absolutePath)
                }

                if (!cacheDir.exists())
                    cacheDir.mkdirs()
                return cacheDir
            }

        val cacheDir: File
            get() {
                val cacheDir = File(packageDir, "cache")
                if (!cacheDir.exists())
                    cacheDir.mkdirs()
                return cacheDir
            }

        fun getChildCacheDir(childDir: String): File {
            if (TextUtils.isEmpty(childDir))
                return cacheDir
            val cacheDir = File(cacheDir, childDir)
            if (!cacheDir.exists())
                cacheDir.mkdirs()
            return cacheDir
        }
    }
}
