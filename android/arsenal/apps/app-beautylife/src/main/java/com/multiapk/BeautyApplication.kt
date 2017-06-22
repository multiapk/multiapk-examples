package com.multiapk

import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager
import android.taobao.atlas.framework.Atlas
import android.taobao.atlas.runtime.ActivityTaskMgr
import android.taobao.atlas.runtime.ClassNotFoundInterceptorCallback
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.squareup.leakcanary.LeakCanary
import org.osgi.framework.BundleException
import org.smartrobot.base.DefaultBaseApplication
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class BeautyApplication : DefaultBaseApplication() {
    override fun onCreate() {
        super.onCreate()
        Log.w("krmao","BeautyApplication:onCreate:"+SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(System.currentTimeMillis()))

        LeakCanary.install(this)

        Atlas.getInstance().setClassNotFoundInterceptorCallback(ClassNotFoundInterceptorCallback { intent ->
            val className = intent.component.className
            val bundleName = AtlasBundleInfoManager.instance().getBundleForComponet(className)

            if (!TextUtils.isEmpty(bundleName) && !AtlasBundleInfoManager.instance().isInternalBundle(bundleName)) {
                //远程bundle
                val activity = ActivityTaskMgr.getInstance().peekTopActivity()
                val remoteBundleFile = File(activity.getExternalCacheDir(), "lib" + bundleName.replace(".", "_") + ".so")

                val path: String
                if (remoteBundleFile.exists()) {
                    path = remoteBundleFile.getAbsolutePath()
                } else {
                    Toast.makeText(activity, " 远程bundle不存在，请确定 : " + remoteBundleFile.getAbsolutePath(), Toast.LENGTH_LONG).show()
                    return@ClassNotFoundInterceptorCallback intent
                }


                val info = activity.getPackageManager().getPackageArchiveInfo(path, 0)
                try {
                    Atlas.getInstance().installBundle(info.packageName, File(path))
                } catch (e: BundleException) {
                    Toast.makeText(activity, " 远程bundle 安装失败，" + e.message, Toast.LENGTH_LONG).show()

                    e.printStackTrace()
                }

                activity.startActivities(arrayOf(intent))

            }

            intent
        })
    }
}
