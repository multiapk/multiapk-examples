package com.multiapk.modules.mobile.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jakewharton.rxbinding2.view.RxView
import org.smartrobot.multiapk.core.hotpatch.Hotpatch
import org.smartrobot.base.DefaultBaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.toast
import java.io.File
import java.util.concurrent.TimeUnit

class AndroidFragment : DefaultBaseFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_android, null)
        val buttonAndroid = contentView.findViewById(R.id.buttonAndroid) as Button
        RxView.clicks(buttonAndroid).debounce(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            Log.w("krmao", "this is android child module" + Thread.currentThread().name)
            try {
                Hotpatch.instance.installPatch("com.multiapk.modules.mobile.ios", 1, File("/storage/emulated/0/ios.patch"))
                context?.toast("合成成功" + Thread.currentThread().name)
            } catch (e: Exception) {
                e.printStackTrace()
                context?.toast("合成失败" + Thread.currentThread().name)
            }
        }
        return contentView
    }
}
