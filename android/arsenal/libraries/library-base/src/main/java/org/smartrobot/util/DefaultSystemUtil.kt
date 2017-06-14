package org.smartrobot.util

import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager

import org.smartrobot.base.DefaultBaseApplication

import java.io.File
import java.lang.reflect.Field


object DefaultSystemUtil {
    /**
     * @return boolean
     * *
     * @title 是否存在 SD 卡
     * *
     * @author michael.mao
     * *
     * @date 2014年10月8日 下午5:29:21
     * *
     * @version V1.0
     */
    val isSdCardExist: Boolean
        get() = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    /**
     * @return String
     * *
     * @title 得到 SD 卡的路径
     * *
     * @author michael.mao
     * *
     * @date 2014年10月8日 下午5:29:30
     * *
     * @version V1.0
     */
    val sdCardPath: String?
        get() {
            if (isSdCardExist)
                return Environment.getExternalStorageDirectory().absolutePath
            return null
        }

    //总大小
    //可用大小
    val sdCardMemory: LongArray
        get() {
            val sdCardInfo = LongArray(2)
            val state = Environment.getExternalStorageState()
            if (Environment.MEDIA_MOUNTED == state) {
                val sdcardDir = Environment.getExternalStorageDirectory()
                val sf = StatFs(sdcardDir.path)
                val bSize = sf.blockSize.toLong()
                val bCount = sf.blockCount.toLong()
                val availBlocks = sf.availableBlocks.toLong()

                sdCardInfo[0] = bSize * bCount
                sdCardInfo[1] = bSize * availBlocks
            }
            return sdCardInfo
        }

    fun sendKeyBackEvent(context: Context) {
        if (context is Activity) {
            val keyEvent = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)
            context.onKeyDown(KeyEvent.KEYCODE_BACK, keyEvent)
        }
    }

    /***
     * 隐藏软键盘
     */
    fun hide(view: View?) {
        if (view != null) {
            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isActive)
                inputMethodManager.hideSoftInputFromWindow(view.applicationWindowToken, 0)
        }
    }

    /***
     * 隐藏软键盘
     */
    fun hide(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (inputMethodManager.isActive)
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /***
     * 隐藏软键盘
     */
    fun show(v: View) {
        val inputMethodManager = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_FORCED)
    }

    /***
     * 隐藏软键盘
     */
    fun toggle(v: View) {
        val inputMethodManager = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    val displayMetrics: DisplayMetrics
        get() = DefaultBaseApplication.instance.resources.displayMetrics

    val density: Float
        get() = displayMetrics.density

    val width: Int
        get() = DefaultBaseApplication.instance.resources.displayMetrics.widthPixels

    val height: Int
        get() = DefaultBaseApplication.instance.resources.displayMetrics.heightPixels


    fun getPxFromDp(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, displayMetrics)
    }

    fun getPxFromPx(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value, displayMetrics)
    }

    fun getPxFromSp(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, displayMetrics)
    }

    val statusBarHeight2: Int
        get() {
            var statusBarHeight = 0
            val resourceId = DefaultBaseApplication.instance.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                statusBarHeight = DefaultBaseApplication.instance.resources.getDimensionPixelSize(resourceId)
            }
            return statusBarHeight
        }

    /* 获取屏幕statusBar高度 */
    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0

        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val field = clazz.getField("status_bar_height")
            val id = Integer.parseInt(field.get(`object`).toString())
            statusBarHeight = context.resources.getDimensionPixelSize(id)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return statusBarHeight
    }

    /**
     * 复制到剪贴板
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun copyToClipboard(label: String, contentText: String): Boolean {
        try {
            val cm = DefaultBaseApplication.instance.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText(label, contentText)
            cm.primaryClip = clip
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }

    }

    /*
    https://developer.android.com/training/system-ui/immersive.html#sticky
    https://developer.android.com/training/system-ui/navigation.html

    This snippet hides the system bars.

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            MSystemUtil.hideSystemUI(getWindow(),false);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.launch_activity);


        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
            if (hasFocus) {
                MSystemUtil.hideSystemUI(getWindow(),false);
            }
        }

     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun hideSystemUI(window: Window, useImmersiveSticky: Boolean) {
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        if (useImmersiveSticky) View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY else View.SYSTEM_UI_FLAG_IMMERSIVE
    }

    // This snippet shows the system bars. It does this by removing all the flags
    // except for the ones that make the content appear under the system bars.
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun showSystemUI(window: Window) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }


    /**
     * 获取设备 SDK版本号
     */
    val sdK_INT: Int
        get() = Build.VERSION.SDK_INT

    //获取设备的信息 包含 IMEI (getDeviceId)
    val telephonyManager: TelephonyManager
        get() = DefaultBaseApplication.instance.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

    /**
     * Returns the unique device ID, for example, the IMEI for GSM and the MEID
     * or ESN for CDMA phones. Return null if device ID is not available.
     *
     *
     *
     * Requires Permission:
     * [READ_PHONE_STATE][android.Manifest.permission.READ_PHONE_STATE]
     */
    val imei: String
        get() = telephonyManager.deviceId

    /**
     * 获取设备的IMSI号
     */
    /**
     * Returns the unique subscriber ID, for example, the IMSI for a GSM phone.
     * Return null if it is unavailable.
     *
     *
     * Requires Permission:
     * [READ_PHONE_STATE][android.Manifest.permission.READ_PHONE_STATE]
     */
    val imsi: String
        get() = telephonyManager.subscriberId

    val versionCode: Int
        get() {
            var versionCode = 0
            try {
                versionCode = DefaultBaseApplication.instance.packageManager.getPackageInfo(DefaultBaseApplication.instance.packageName, 0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return versionCode
        }

    val versionName: String?
        get() {
            var versionName: String? = null
            try {
                versionName = DefaultBaseApplication.instance.packageManager.getPackageInfo(DefaultBaseApplication.instance.packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return versionName
        }

    fun getAppMetaData(key: String): String? {
        var metaData: String? = null
        try {
            val info = DefaultBaseApplication.instance.packageManager.getApplicationInfo(DefaultBaseApplication.instance.packageName, PackageManager.GET_META_DATA)
            metaData = info.metaData.getString(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return metaData
    }

    fun getAppMetaDataForInt(key: String): Int {
        var metaData = 0
        try {
            val info = DefaultBaseApplication.instance.packageManager.getApplicationInfo(DefaultBaseApplication.instance.packageName, PackageManager.GET_META_DATA)
            metaData = info.metaData.getInt(key)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return metaData
    }

    fun isAppInstalled(context: Context?, pkgName: String): Boolean {
        if (context != null) {
            try {
                context.packageManager.getPackageInfo(pkgName, PackageManager.PERMISSION_GRANTED)
                return true
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

        }
        return false
    }

    fun isBackground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses
        for (appProcess in appProcesses) {
            if (appProcess.processName == context.packageName) {
                /* BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200*/
                Log.i(context.packageName, "此appimportace =" + appProcess.importance + ",context.getClass().getName()=" + context.javaClass.name)
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.packageName, "处于后台" + appProcess.processName)
                    return true
                } else {
                    Log.i(context.packageName, "处于前台" + appProcess.processName)
                    return false
                }
            }
        }
        return false
    }
}
