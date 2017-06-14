package org.smartrobot.base

import android.app.Activity
import android.app.Application
import android.os.Bundle

class DefaultActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    // I use four separate variables here. You can, of course, just use two and
    // increment/decrement them instead of using four and incrementing them all.
    /*private int resumed;
    private int paused;
    private int started;
    private int stopped;*/

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onActivityResumed(activity: Activity) {
        ++resumed
    }

    override fun onActivityPaused(activity: Activity) {
        ++paused
        android.util.Log.w("test", "application is in foreground: " + (resumed > paused))
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        ++started
    }

    override fun onActivityStopped(activity: Activity) {
        ++stopped
        android.util.Log.w("test", "application is visible: " + (started > stopped))
    }

    companion object {

        // If you want a static function you can use to check if your application is
        // foreground/background, you can use the following:

        // Replace the four variables above with these four
        private var resumed: Int = 0
        private var paused: Int = 0
        private var started: Int = 0
        private var stopped: Int = 0

        // And these two public static functions
        val isApplicationVisible: Boolean
            get() = started > stopped

        val isApplicationInForeground: Boolean
            get() = resumed > paused
    }

}