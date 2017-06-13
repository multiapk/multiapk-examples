package org.smartrobot.widget


import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * 能够兼容ViewPager的ScrollView
 *
 *
 * 解决了ViewPager在ScrollView中的滑动反弹问题
 */
class DefaultScrollView : android.widget.ScrollView {

    interface OnScrollListener {
        fun onScrollChanged(x: Int, y: Int, oldX: Int, oldY: Int)

        fun onScrollStopped()

        fun onScrolling()
    }


    protected var xDistance: Float = 0.toFloat()
    protected var yDistance: Float = 0.toFloat()
    protected var xLast: Float = 0.toFloat()
    protected var yLast: Float = 0.toFloat()
    protected var onScrollListener: OnScrollListener? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                yDistance = 0f
                xDistance = yDistance
                xLast = ev.x
                yLast = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val curX = ev.x
                val curY = ev.y

                xDistance += Math.abs(curX - xLast)
                yDistance += Math.abs(curY - yLast)
                xLast = curX
                yLast = curY

                if (xDistance > yDistance) {
                    return false
                }
            }
        }

        return super.onInterceptTouchEvent(ev)
    }

    fun setOnScrollListener(onScrollListener: OnScrollListener) {
        this.onScrollListener = onScrollListener
    }

    override fun onScrollChanged(x: Int, y: Int, oldX: Int, oldY: Int) {
        super.onScrollChanged(x, y, oldX, oldY)
        if (onScrollListener != null)
            onScrollListener!!.onScrollChanged(x, y, oldX, oldY)
    }

    fun isChildVisible(child: View?): Boolean {
        if (child == null)
            return false
        val scrollBounds = Rect()
        getHitRect(scrollBounds)
        return child.getLocalVisibleRect(scrollBounds)
    }

    val isAtTop: Boolean
        get() = scrollY <= 0

    val isAtBottom: Boolean
        get() = getChildAt(childCount - 1).bottom + paddingBottom == height + scrollY
}