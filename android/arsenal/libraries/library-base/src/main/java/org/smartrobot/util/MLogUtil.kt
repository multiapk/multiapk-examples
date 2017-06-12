/**
 * @title MLog.java
 * *
 * @author michael.mao
 * *
 * @date 2014年3月26日 下午1:12:34
 * *
 * @version V1.0
 */
package org.smartrobot.util

import android.text.TextUtils
import android.util.Log

object MLogUtil {
    private var isDebugging = true

    fun setDebugAble(isDebug: Boolean) {
        isDebugging = isDebug
    }

    @JvmOverloads fun v(tag: String, msg: String, tr: Throwable? = null) {
        if (isDebugging) {
            Log.v(tag, msg, tr)
        }
    }

    @JvmOverloads fun d(tag: String, msg: String, tr: Throwable? = null) {
        if (isDebugging) {
            Log.d(tag, msg, tr)
        }
    }

    @JvmOverloads fun i(tag: String, msg: String, tr: Throwable? = null) {
        if (isDebugging) {
            Log.i(tag, msg, tr)
        }
    }

    @JvmOverloads fun w(tag: String, msg: String, tr: Throwable? = null) {
        if (isDebugging) {
            Log.w(tag, msg, tr)
        }
    }

    @JvmOverloads fun e(tag: String, msg: String, tr: Throwable? = null) {
        if (isDebugging) {
            Log.e(tag, msg, tr)
        }
    }

    private val LOG_DIR_NAME = "log"

    /*val cacheDir: File
        get() {
            val cacheDir = File(MGlobalCacheManager.getCacheDir(), if (DefaultBaseApplication.INSTANCE.isDebugModel()) LOG_DIR_NAME else MMD5Util.getMessageDigest(LOG_DIR_NAME.toByteArray()))
            if (!cacheDir.exists())
                cacheDir.mkdirs()
            return cacheDir
        }

    @JvmOverloads fun write(tag: String, msg: String, tr: Throwable? = null) {
        DefaultFileUtil.writeTextToFile(tag + ":\n" + msg + "\n", tr, File(cacheDir, "MLog_" + SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.getDefault()).format(Date(System.currentTimeMillis())) + ".txt"))
    }*/

    val location: String
        get() {
            val className = MLogUtil::class.java.name
            val traces = Thread.currentThread()
                    .stackTrace
            var found = false

            for (trace in traces) {
                try {
                    if (found) {
                        if (!trace.className.startsWith(className)) {
                            val clazz = Class.forName(trace.className)
                            return "[" + getClassName(clazz) + ":" + trace.methodName + ":" + trace.lineNumber.toString() + "]: "
                        }
                    } else if (trace.className.startsWith(className)) {
                        found = true
                    }
                } catch (ignored: ClassNotFoundException) {
                }

            }
            return "[]: "
        }

    private fun getClassName(clazz: Class<*>?): String? {
        if (clazz != null) {
            if (!TextUtils.isEmpty(clazz.simpleName))
                return clazz.simpleName
            return getClassName(clazz.enclosingClass)
        }
        return null
    }
}
