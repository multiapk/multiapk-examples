package com.multiapk.modules.order

import android.app.Application
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class OrderApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.w("krmao","OrderApplication:onCreate:"+ SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(System.currentTimeMillis()))
    }
}