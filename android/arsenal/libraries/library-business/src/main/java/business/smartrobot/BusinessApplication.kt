package business.smartrobot

import android.app.Application
import android.util.Log
import business.smartrobot.api.DefaultApiManager
import business.smartrobot.database.dao.DaoMaster
import business.smartrobot.database.dao.DaoSession
import business.smartrobot.database.model.Order
import org.greenrobot.greendao.database.Database
import org.smartrobot.base.DefaultBaseApplication

class BusinessApplication : Application() {
    companion object {
        lateinit var instance: BusinessApplication
        lateinit var daoSession: DaoSession
        val DATABASE_NAME = "smart-robot"
    }

    override fun onCreate() {
        super.onCreate()
        Log.w("krmao", "BusinessApplication:onCreate:" + System.currentTimeMillis())
        BusinessApplication.instance = this
        initDatabase()
        DefaultApiManager.init(DefaultBaseApplication.isDebugModel)
        addTestData()
    }

    fun initDatabase() {
        val helper = object : DaoMaster.OpenHelper(this, DATABASE_NAME) {
            override fun onUpgrade(db: Database?, oldVersion: Int, newVersion: Int) {
                super.onUpgrade(db, oldVersion, newVersion)
            }
        }
        daoSession = DaoMaster(helper.writableDb).newSession()
    }

    fun addTestData() {
        while (daoSession.orderDao.loadAll().size < 20) {
            daoSession.orderDao.insert(Order())
        }
    }
}
