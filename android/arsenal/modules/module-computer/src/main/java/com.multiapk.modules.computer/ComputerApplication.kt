package com.multiapk.modules.computer

import android.app.Application
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class ComputerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.w("krmao","ComputerApplication:onCreate:"+ SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(System.currentTimeMillis()))
    }
}