package com.multiapk.modules.mobile.android

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mlibrary.multiapk.core.hotpatch.Hotpatch
import com.multiapk.library.base.CMFragment
import org.jetbrains.anko.toast
import java.io.File

class AndroidFragment : CMFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_android, null)
        val buttonAndroid = contentView.findViewById(R.id.buttonAndroid) as Button
        buttonAndroid.setOnClickListener {
            try {
                Hotpatch.instance.installPatch("com.multiapk.modules.mobile.ios", 1, File("/storage/emulated/0/ios.patch"))
                context?.toast("合成成功")
            } catch (e: Exception) {
                e.printStackTrace()
                context?.toast("合成失败")
            }
        }
        return contentView
    }
}
