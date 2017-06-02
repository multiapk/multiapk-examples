package com.multiapk.modules.mobile.ios

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jakewharton.rxbinding2.view.RxView
import com.multiapk.library.base.CMFragment
import org.jetbrains.anko.toast
import java.util.concurrent.TimeUnit


class IosFragment : CMFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_ios, null)
        val buttonIos = contentView.findViewById(R.id.buttonIos) as Button
        RxView.clicks(buttonIos).throttleFirst(1, TimeUnit.SECONDS).subscribe {
            context?.toast("this is ios child module")
        }
        return contentView
    }
}
