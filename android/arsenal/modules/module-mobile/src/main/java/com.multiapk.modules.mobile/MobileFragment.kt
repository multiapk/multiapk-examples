package com.multiapk.modules.mobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jakewharton.rxbinding2.view.RxView
import org.smartrobot.base.DefaultActivity
import org.smartrobot.base.DefaultBaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class MobileFragment : DefaultBaseFragment() {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_mobile, null)
        val buttonAndroid = contentView.findViewById(R.id.buttonAndroid) as Button
        val buttonIos = contentView.findViewById(R.id.buttonIos) as Button

        compositeDisposable.add(RxView.clicks(buttonAndroid).debounce(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            Log.w("krmao", "跳转安卓模块:" + Thread.currentThread().name)
//            context?.toast("跳转安卓模块:" + Thread.currentThread().name)
            DefaultActivity.start(activity, "com.multiapk.modules.mobile.android.AndroidFragment")
        })
        compositeDisposable.add(RxView.clicks(buttonIos).throttleLast(200, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe {
            Log.w("krmao", "跳转苹果模块:" + Thread.currentThread().name)
//            context?.toast("跳转苹果模块" + Thread.currentThread().name)
            DefaultActivity.start(activity, "com.multiapk.modules.mobile.ios.IosFragment")
        })
        return contentView
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
