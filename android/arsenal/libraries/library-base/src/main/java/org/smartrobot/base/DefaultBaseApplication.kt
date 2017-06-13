package org.smartrobot.base

import android.app.Application
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatDelegate
import com.mlibrary.multiapk.MultiApk
import org.greenrobot.greendao.database.Database
import org.smartrobot.BuildConfig
import org.smartrobot.database.dao.DaoMaster
import org.smartrobot.database.dao.DaoSession
import org.smartrobot.util.DefaultLogUtil


open class DefaultBaseApplication : Application() {

    companion object {
        lateinit var INSTANCE: DefaultBaseApplication
    }

    override fun onCreate() {
        if (!BuildConfig.solidMode)
            MultiDex.install(this)
        else
            MultiApk.init(this)
        super.onCreate()
        INSTANCE = this

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//selector vector support
        // Simply add the handler, and that's it! No need to add any code
        // to every activity. Everything is contained in MActivityLifecycleCallbacks
        // with just a few lines of code. Now *that's* nice.
        registerActivityLifecycleCallbacks(DefaultActivityLifecycleCallbacks())
        DefaultLogUtil.setDebugAble(BuildConfig.solidMode)
        initDatabase()
    }

    private lateinit var daoSession: DaoSession
    private val DATABASE_NAME = "smart-robot"

    fun initDatabase() {
        val helper = object : DaoMaster.OpenHelper(this, DATABASE_NAME) {
            override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
                super.onUpgrade(db, oldVersion, newVersion)
            }
        }
        daoSession = DaoMaster(helper.writableDb).newSession()
    }

    fun getDaoSession(): DaoSession {
        return daoSession
    }
}