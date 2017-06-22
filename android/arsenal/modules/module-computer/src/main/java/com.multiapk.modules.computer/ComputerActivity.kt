package com.multiapk.modules.computer

import android.os.Bundle
import android.widget.FrameLayout
import org.smartrobot.base.DefaultBaseActivity

class ComputerActivity : DefaultBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(FrameLayout(this))
        supportFragmentManager.beginTransaction().add(android.R.id.content, ComputerFragment(), ComputerFragment::javaClass.name).commitAllowingStateLoss()
    }
}
