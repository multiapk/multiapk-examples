package com.multiapk.modules.home

import android.Manifest
import android.app.Activity
import android.os.Bundle
import android.support.annotation.NonNull
import android.util.Log
import com.multiapk.R
import com.multiapk.library.base.MCommonActivity
import com.multiapk.library.base.MFragmentActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import com.tbruyelle.rxpermissions2.*
import io.reactivex.functions.Consumer
import android.support.v7.widget.CardView

class MainKotlinActivity : MFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        cardViewComputerModule.setOnClickListener {
            longToast("电脑模块")
            MCommonActivity.start(this, "com.multiapk.modules.computer.ComputerFragment")
        }
        cardViewMobileModule.setOnClickListener {
            toast("手机模块")
            MCommonActivity.start(this, "com.multiapk.modules.mobile.MobileFragment")
        }

        val rxPermissions = RxPermissions(this)
        rxPermissions.request(Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS).subscribe { granted ->
            if (granted!!) {
                // All permissions were granted//
                Log.d("krmao", "All permissions were granted")
            } else {
                //One or more permissions was denied//
                Log.d("krmao", "One or more permissions was denied")
            }
        }
        rxPermissions.request(Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS).subscribe({ granted ->
            if (granted!!) {
                // All permissions were granted//
                Log.d("krmao", "All permissions were granted")
            } else {
                //One or more permissions was denied//
                Log.d("krmao", "One or more permissions was denied")
            }

        })
        rxPermissions.setLogging(true)
        rxPermissions.requestEach(Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE).subscribe({ permission ->
            if (permission.granted) {
                // `permission.name` is granted !
                Log.d("krmao", "permission:" + permission.name + " is granted")
            } else if (permission.shouldShowRequestPermissionRationale) {
                // Denied permission without ask never again
                Log.d("krmao", "Denied permission without ask never again")
                //rxPermissions.shouldShowRequestPermissionRationale(HomeActivity.this, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE);
            } else {
                // Denied permission with ask never again
                // Need to go to the settings
                Log.d("krmao", "Need to go to the settings")
            }
        })
    }
}