package org.smartrobot.util

import android.app.Activity
import android.content.ComponentName
import android.content.Intent

/***
 * @title 创建快捷方式
 * *
 * @author michael.mao
 * *
 * @date 2014年10月15日 上午10:24:21
 * *
 * @version V1.0
 */
object DefaultShortcutUtil {
    // <uses-permission
    // android:name="com.android.launcher.permission.INSTALL_SHORTCUT"/>
    /***
     * @title 创建桌面快捷方式
     * *
     * @param activity
     * *
     * @param name
     * *
     * @param ic_launcher
     * *            void
     * *
     * @author michael.mao
     * *
     * @date 2014年10月15日 上午10:07:00
     * *
     * @version V1.0
     */
    fun createShortCut(activity: Activity, name: String, ic_launcher: Int) {
        val shortcutIntent = Intent("com.android.launcher.action.INSTALL_SHORTCUT")
        // 快捷方式的名称
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name)
        shortcutIntent.putExtra("duplicate", false) // 不允许重复创建

        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
        val pkgName = activity.packageName
        intent.component = ComponentName(pkgName, pkgName + "." + activity.localClassName)
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent)

        // 快捷方式的图标
        val iconRes = Intent.ShortcutIconResource.fromContext(activity, ic_launcher)
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes)
        activity.sendBroadcast(shortcutIntent)
    }
}
