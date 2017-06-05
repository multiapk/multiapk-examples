package com.multiapk.modules.computer

import com.facebook.react.ReactActivity

class MyReactActivity : ReactActivity() {
    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    override fun getMainComponentName(): String? {
        return "HelloWorld"
    }
}