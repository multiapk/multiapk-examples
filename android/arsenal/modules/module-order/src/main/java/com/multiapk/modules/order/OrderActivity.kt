package com.multiapk.modules.order

import android.os.Bundle
import org.smartrobot.base.DefaultBaseActivity

class OrderActivity : DefaultBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_order)
    }
}
