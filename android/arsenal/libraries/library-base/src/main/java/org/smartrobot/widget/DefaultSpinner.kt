package org.smartrobot.widget

import android.content.Context
import android.graphics.Point
import android.support.v7.widget.AppCompatSpinner
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/***
 * 增加 onClickListener ，点击事件发生后，默认不再打开列表选择框，如需打开，请调用
 * [.performClick]
 */
class DefaultSpinner : AppCompatSpinner {
    private val oldPoint = Point()
    private var onClickListener: View.OnClickListener? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                oldPoint.x = event.x.toInt()
                oldPoint.y = event.y.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> if (Math.abs(event.x.toInt() - oldPoint.x) < 20 && Math.abs(event.y.toInt() - oldPoint.y) < 20 && isEnabled) {
                post { onClickListener!!.onClick(null) }
            }
            else -> {
            }
        }
        return true
    }

    override fun setOnClickListener(listener: View.OnClickListener?) {
        this.onClickListener = listener
    }
}
