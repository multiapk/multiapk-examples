package com.multiapk.modules.computer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.toast
import org.smartrobot.base.DefaultBaseFragment

class ComputerFragment : DefaultBaseFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.computer_fragment_computer, null)
        context?.toast("cici is nice")
        /*RxView.clicks(contentView).subscribe {
            context?.toast("电脑模块" + Thread.currentThread().name)
        }*/
        return contentView
    }
}
