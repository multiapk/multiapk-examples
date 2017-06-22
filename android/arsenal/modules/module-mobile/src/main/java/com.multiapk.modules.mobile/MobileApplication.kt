package com.multiapk.modules.mobile

import android.app.Application
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class MobileApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.w("krmao","MobileApplication:onCreate:"+ SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(System.currentTimeMillis()))
    }
}