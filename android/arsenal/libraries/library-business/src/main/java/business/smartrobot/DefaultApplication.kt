package business.smartrobot

import business.smartrobot.api.DefaultApiManager
import business.smartrobot.database.dao.DaoMaster
import business.smartrobot.database.dao.DaoSession
import com.squareup.leakcanary.LeakCanary
import org.greenrobot.greendao.database.Database
import org.smartrobot.base.DefaultBaseApplication

class DefaultApplication : DefaultBaseApplication() {
    companion object {
        lateinit var instance: DefaultApplication
    }

    override fun onCreate() {
        super.onCreate()
        DefaultApplication.instance = this
        LeakCanary.install(this)
        initDatabase()
        DefaultApiManager.init(isDebugModel)
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
