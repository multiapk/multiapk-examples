package org.smartrobot.widget.debug

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.support.annotation.DrawableRes
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView

import com.mlibrary.util.DefaultSystemUtil
import com.mlibrary.util.DefaultPreferencesUtil
import com.mlibrary.util.manager.DefaultCacheManager

import org.smartrobot.R
import org.smartrobot.base.DefaultBaseApplication

/*
<!--悬浮窗-->
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
*/
enum class DefaultFloatViewUtil private constructor() {
    instance;

    init {
        floatView = ImageView(DefaultBaseApplication.INSTANCE)
        floatView.setImageResource(R.drawable.default_emo_im_happy)
        windowManager = DefaultBaseApplication.INSTANCE.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowLayoutParams = WindowManager.LayoutParams()
        windowLayoutParams.type = WindowManager.LayoutParams.TYPE_TOAST
        windowLayoutParams.format = PixelFormat.RGBA_8888
        windowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

        windowLayoutParams.width = defaultWH
        windowLayoutParams.height = defaultWH
        windowLayoutParams.gravity = Gravity.START or Gravity.TOP
        windowLayoutParams.x = DefaultPreferencesUtil.getInstance().getInt(KEY_LAST_X, defaultX)
        windowLayoutParams.y = DefaultPreferencesUtil.getInstance().getInt(KEY_LAST_Y, defaultY)
        floatView.setOnTouchListener(object : View.OnTouchListener {
            private var x: Float = 0.toFloat()
            private var y: Float = 0.toFloat()
            private var rawX: Float = 0.toFloat()
            private var rawY: Float = 0.toFloat()

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        floatView.setImageResource(R.drawable.default_emo_im_foot_in_mouth)
                        x = event.x
                        y = event.y
                        rawX = event.rawX
                        rawY = event.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        windowLayoutParams.x = (event.rawX - x).toInt()
                        windowLayoutParams.y = (event.rawY - DefaultSystemUtil.statusBarHeight2.toFloat() - y).toInt()
                        windowManager.updateViewLayout(v, windowLayoutParams)
                    }
                    MotionEvent.ACTION_UP -> {
                        floatView.setImageResource(R.drawable.default_emo_im_happy)
                        DefaultPreferencesUtil.getInstance().putInt(KEY_LAST_X, windowLayoutParams.x)
                        DefaultPreferencesUtil.getInstance().putInt(KEY_LAST_Y, windowLayoutParams.y)
                        if (Math.abs(event.rawX - rawX) < 5 && Math.abs(event.rawY - rawY) < 5) {
                            if (listener != null)
                                listener!!.onClick(floatView)
                            else
                                DefaultDebugFragment.goTo(object : DefaultCacheManager.OnCacheCallBack<Any> {
                                    override fun onSuccess(successObject: Any) {

                                    }

                                    override fun onFailure(failureObject: Any) {

                                    }
                                })
                        }
                    }
                }
                return true
            }
        })
    }

    private val KEY_LAST_X = "M_FLOAT_VIEW_LAST_X"
    private val KEY_LAST_Y = "M_FLOAT_VIEW_LAST_Y"
    private val KEY_HIDE_ALWAYS = "KEY_HIDE_ALWAYS"
    private val defaultWH = DefaultSystemUtil.getPxFromDp(42f).toInt()
    private val defaultX = 0
    private val defaultY = defaultWH * 5

    private val floatView: ImageView? = null
    private val windowManager: WindowManager?
    private val windowLayoutParams: WindowManager.LayoutParams
    private var listener: View.OnClickListener? = null

    var isAwaysHide: Boolean
        get() = DefaultPreferencesUtil.getInstance().getBoolean(KEY_HIDE_ALWAYS, false)
        set(isAwaysHide) {
            DefaultPreferencesUtil.getInstance().putBoolean(KEY_HIDE_ALWAYS, isAwaysHide)
            if (isAwaysHide)
                hide()
        }

    fun setImageResource(@DrawableRes resId: Int) {
        floatView?.setImageResource(resId)
    }

    fun setOnClickListener(listener: View.OnClickListener?) {
        this.listener = listener
    }

    fun show() {
        try {
            hide()
            if (!isAwaysHide)
                windowManager!!.addView(floatView, windowLayoutParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    fun clearLocationCatch() {
        DefaultPreferencesUtil.getInstance().putInt(KEY_LAST_X, defaultX)
        DefaultPreferencesUtil.getInstance().putInt(KEY_LAST_Y, defaultY)
    }

    fun hide() {
        try {
            if (floatView != null && windowManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    if (floatView.isAttachedToWindow)
                        windowManager.removeViewImmediate(floatView)
                } else {
                    if (floatView.parent != null)
                        windowManager.removeViewImmediate(floatView)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
