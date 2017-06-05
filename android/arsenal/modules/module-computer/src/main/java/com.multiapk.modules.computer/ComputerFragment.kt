package com.multiapk.modules.computer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView

import org.smartrobot.base.DefaultBaseFragment
import org.jetbrains.anko.toast

class ComputerFragment : DefaultBaseFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_computer, null)
        RxView.clicks(contentView).subscribe {
            context?.toast("电脑模块" + Thread.currentThread().name)
            startActivity(Intent(context, MyReactActivity::class.java))
        }
        return contentView
    }
}
