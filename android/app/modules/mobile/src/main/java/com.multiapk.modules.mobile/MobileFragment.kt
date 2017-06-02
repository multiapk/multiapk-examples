package com.multiapk.modules.mobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jakewharton.rxbinding2.view.RxView

import com.multiapk.library.base.CMCommonActivity
import com.multiapk.library.base.CMFragment
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit


class MobileFragment : CMFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_mobile, null)
        val buttonAndroid = contentView.findViewById(R.id.buttonAndroid) as Button
        val buttonIos = contentView.findViewById(R.id.buttonIos) as Button

        RxView.clicks(buttonAndroid).throttleLast(1, TimeUnit.SECONDS).subscribe {
            context?.toast("跳转安卓模块")
            CMCommonActivity.start(activity, "com.multiapk.modules.mobile.android.AndroidFragment")
        }
        RxView.clicks(buttonIos).throttleLast(1, TimeUnit.SECONDS).subscribe {
            context?.toast("跳转苹果模块")
            CMCommonActivity.start(activity, "com.multiapk.modules.mobile.ios.IosFragment")
        }
        return contentView
    }
}
