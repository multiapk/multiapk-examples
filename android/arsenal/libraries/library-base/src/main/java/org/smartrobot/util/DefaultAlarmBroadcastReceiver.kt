package org.smartrobot.util

import android.content.BroadcastReceiver

/*
    必须静态注册，动态注册没有任何作用
    <receiver android:name=".modules.mine.MAlarmBroadcastReceiver">
        <intent-filter>
            <action android:name="org.smartrobot.action.alarm" />
        </intent-filter>
    </receiver>
 */
abstract class DefaultAlarmBroadcastReceiver : BroadcastReceiver() {
    protected var TAG = javaClass.simpleName

    companion object {
        val ACTION = "org.smartrobot.action.alarm"
    }
}
