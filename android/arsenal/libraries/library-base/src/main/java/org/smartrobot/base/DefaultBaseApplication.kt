package org.smartrobot.base

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import org.smartrobot.util.DefaultLogUtil
import org.smartrobot.util.DefaultSystemUtil
import java.text.SimpleDateFormat
import java.util.*

open class DefaultBaseApplication : Application() {
    val KEY_DEBUG_MODEL: String = "debugModel"

    companion object {
        lateinit var instance: DefaultBaseApplication
        var isDebugModel: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()
        Log.w("krmao","DefaultBaseApplication:onCreate:"+ SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(System.currentTimeMillis()))
        instance = this
        isDebugModel = DefaultSystemUtil.getAppMetaData(KEY_DEBUG_MODEL) as Boolean

        DefaultLogUtil.setDebugAble(isDebugModel)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//selector vector support
        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in MActivityLifecycleCallbacks
        // with just a few lines of code. Now *that's* nice.
        registerActivityLifecycleCallbacks(DefaultActivityLifecycleCallbacks())
    }
}