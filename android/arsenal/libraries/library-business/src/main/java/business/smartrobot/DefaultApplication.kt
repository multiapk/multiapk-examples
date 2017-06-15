package business.smartrobot

import business.smartrobot.api.DefaultApiManager
import business.smartrobot.database.dao.DaoMaster
import business.smartrobot.database.dao.DaoSession
import com.facebook.react.ReactApplication
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.shell.MainReactPackage
import com.facebook.soloader.SoLoader
import com.squareup.leakcanary.LeakCanary
import org.greenrobot.greendao.database.Database
import org.smartrobot.base.DefaultBaseApplication
import java.util.*

class DefaultApplication : DefaultBaseApplication(), ReactApplication {
    companion object {
        lateinit var instance: DefaultApplication
    }

    override fun onCreate() {
        super.onCreate()
        DefaultApplication.instance = this
        LeakCanary.install(this)

        SoLoader.init(this, false)
        initDatabase()
        DefaultApiManager.init()
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

    override fun getReactNativeHost(): ReactNativeHost {
        return object : ReactNativeHost(this) {
            override fun getUseDeveloperSupport(): Boolean {
                return BuildConfig.DEBUG
            }

            override fun getPackages(): List<ReactPackage> {
                return Arrays.asList<ReactPackage>(
                        MainReactPackage()
                )
            }
        }
    }
}
