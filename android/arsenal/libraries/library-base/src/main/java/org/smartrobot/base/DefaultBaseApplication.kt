package org.smartrobot.base

import android.app.Application
import android.content.Intent
import android.support.v7.app.AppCompatDelegate
import android.taobao.atlas.bundleInfo.AtlasBundleInfoManager
import android.taobao.atlas.framework.Atlas
import android.taobao.atlas.runtime.ActivityTaskMgr
import android.taobao.atlas.runtime.ClassNotFoundInterceptorCallback
import android.text.TextUtils
import android.widget.Toast
import org.osgi.framework.BundleException
import org.smartrobot.util.DefaultLogUtil
import org.smartrobot.util.DefaultSystemUtil
import java.io.File

open class DefaultBaseApplication : Application() {
    val KEY_DEBUG_MODEL: String = "debugModel"

    companion object {
        lateinit var instance: DefaultBaseApplication
        var isDebugModel: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        isDebugModel = DefaultSystemUtil.getAppMetaData(KEY_DEBUG_MODEL) as Boolean

        DefaultLogUtil.setDebugAble(isDebugModel)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//selector vector support
        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in MActivityLifecycleCallbacks
        // with just a few lines of code. Now *that's* nice.
        registerActivityLifecycleCallbacks(DefaultActivityLifecycleCallbacks())

        Atlas.getInstance().setClassNotFoundInterceptorCallback(object : ClassNotFoundInterceptorCallback {
            override fun returnIntent(intent: Intent): Intent {
                val className = intent.component.className
                val bundleName = AtlasBundleInfoManager.instance().getBundleForComponet(className)

                if (!TextUtils.isEmpty(bundleName) && !AtlasBundleInfoManager.instance().isInternalBundle(bundleName)) {

                    //远程bundle
                    val activity = ActivityTaskMgr.getInstance().peekTopActivity()
                    val remoteBundleFile = File(activity.getExternalCacheDir(), "lib" + bundleName.replace(".", "_") + ".so")

                    var path = ""
                    if (remoteBundleFile.exists()) {
                        path = remoteBundleFile.getAbsolutePath()
                    } else {
                        Toast.makeText(activity, " 远程bundle不存在，请确定 : " + remoteBundleFile.getAbsolutePath(), Toast.LENGTH_LONG).show()
                        return intent
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

                return intent
            }
        })
    }
}