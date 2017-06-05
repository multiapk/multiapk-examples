package com.multiapk.modules.mobile.ios

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jakewharton.rxbinding2.view.RxView
import org.smartrobot.base.DefaultBaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit


class IosFragment : DefaultBaseFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_ios, null)
        val buttonIos = contentView.findViewById(R.id.buttonIos) as Button
        RxView.clicks(buttonIos).debounce(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            Log.w("krmao", "this is ios child module" + Thread.currentThread().name)
            context?.toast("this is ios child module" + Thread.currentThread().name)
        }
        return contentView
    }
}
