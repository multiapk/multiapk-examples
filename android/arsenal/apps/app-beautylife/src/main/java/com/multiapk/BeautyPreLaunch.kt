package com.multiapk

import android.content.Context
import android.taobao.atlas.runtime.AtlasPreLauncher
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class BeautyPreLaunch : AtlasPreLauncher {
    override fun initBeforeAtlas(context: Context) {
        Log.d("krmao", "BeautyPreLaunch:initBeforeAtlas:"+ SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(System.currentTimeMillis()))
    }
}
