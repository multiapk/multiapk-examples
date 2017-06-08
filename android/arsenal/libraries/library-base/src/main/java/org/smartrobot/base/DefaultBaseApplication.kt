package org.smartrobot.base

import android.app.Application
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatDelegate
import com.mlibrary.multiapk.MultiApk
import com.multiapk.library.BuildConfig
import org.greenrobot.greendao.database.Database
import org.smartrobot.database.dao.DaoMaster
import org.smartrobot.database.dao.DaoSession


open class DefaultBaseApplication : Application() {

    companion object {
        lateinit var instance: DefaultBaseApplication
    }

    override fun onCreate() {
        if (!BuildConfig.solidMode)
            MultiDex.install(this)
        else
            MultiApk.init(this)
        super.onCreate()
        instance = this

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//selector vector support

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