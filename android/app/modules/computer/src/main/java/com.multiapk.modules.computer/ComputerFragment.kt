package com.multiapk.modules.computer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.RxView

import com.multiapk.library.base.CMFragment
import org.jetbrains.anko.toast

class ComputerFragment : CMFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_computer, null)
        RxView.clicks(contentView).subscribe {
            context?.toast("电脑模块")
            startActivity(Intent(context, MyReactActivity::class.java))
        }
        return contentView
    }
}
