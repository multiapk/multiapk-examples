package com.multiapk.modules.home

import android.Manifest
import android.os.Bundle
import android.util.Log
import com.multiapk.R
import com.multiapk.base.DefaultApplication
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.rxkotlin.toFlowable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import org.multiapk.library.base.DefaultActivity
import org.multiapk.library.base.DefaultBaseActivity
import org.smartrobot.database.model.Note
import org.smartrobot.database.model.NoteType
import java.util.*


class HomeActivity : DefaultBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        cardViewComputerModule.setOnClickListener {
            longToast("电脑模块")
            DefaultActivity.start(this, "com.multiapk.modules.computer.ComputerFragment")
        }
        cardViewMobileModule.setOnClickListener {
            toast("手机模块")
            DefaultActivity.start(this, "com.multiapk.modules.mobile.MobileFragment")
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

        testDB()
        val daoSession = (application as DefaultApplication).getDaoSession()
        val noteDao = daoSession?.getNoteDao()

        noteDao?.loadAll()?.toFlowable()?.subscribeOn(Schedulers.io())?.subscribe { note ->
            Log.w("krmao", "db:note: " + (note as Note?).toString())
        }
    }

    fun testDB() {
        val daoSession = (application as DefaultApplication).getDaoSession()
        val noteDao = daoSession?.getNoteDao()

        val note = Note()
        note.setText("test")
        note.setComment("aa")
        note.setDate(Date())
        note.setType(NoteType.TEXT)
        noteDao?.insert(note)
        Log.d("DaoExample", "Inserted new note, ID: " + note.id)
    }
}