package org.smartrobot.util

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import org.smartrobot.R
import org.smartrobot.base.DefaultBaseApplication

class DefaultToastUtil private constructor() {

    init {
        throw AssertionError()
    }

    private class ViewHolder(var contentLayout: View, var textView: TextView)

    companion object {

        private var toast: Toast? = null

        fun show(strId: Int) {
            show(DefaultBaseApplication.instance.resources.getString(strId))
        }

        fun show(msg: String, defaultStr: String) {
            show(if (TextUtils.isEmpty(msg)) defaultStr else msg)
        }

        @SuppressLint("ShowToast")
        fun show(msg: String) {
            if (!TextUtils.isEmpty(msg)) {
                if (toast == null) {
                    toast = Toast.makeText(DefaultBaseApplication.instance, msg, Toast.LENGTH_SHORT)
                    toast!!.view = toast!!.view
                    toast!!.duration = Toast.LENGTH_SHORT
                } else {
                    toast!!.setText(msg)
                }
                toast!!.show()
            }
        }

        fun showDefault(msg: String) {
            if (!TextUtils.isEmpty(msg))
                Toast.makeText(DefaultBaseApplication.instance, msg, Toast.LENGTH_SHORT).show()
        }

        private var mToastCount = 5

        fun showMore(message: String) {
            if (TextUtils.isEmpty(message))
                return
            val toast = Toast(DefaultBaseApplication.instance)
            val viewHolder = toastLayout
            if (viewHolder != null) {
                viewHolder.textView.text = message
                toast.view = viewHolder.contentLayout
                toast.duration = computeShowTime(viewHolder.textView, message)
                val averageDip = DefaultSystemUtil.height / 7
                when (mToastCount) {
                    5 -> toast.setGravity(Gravity.TOP, 0, 0)
                    4 -> toast.setGravity(Gravity.TOP, 0, averageDip)
                    3 -> toast.setGravity(Gravity.TOP, 0, 2 * averageDip)
                    2 -> toast.setGravity(Gravity.TOP, 0, 3 * averageDip)
                    1 -> toast.setGravity(Gravity.TOP, 0, 4 * averageDip)
                    else -> toast.setGravity(Gravity.CENTER, 0, 0)
                }
                toast.show()
                mToastCount--
            }
        }

        private fun computeShowTime(tv: TextView, msg: String): Int {
            val paint = tv.paint ?: return Toast.LENGTH_SHORT
            val maxWidth = (DefaultSystemUtil.width - DefaultSystemUtil.getPxFromDp(50f)).toInt()
            val msgLength = paint.measureText(msg)
            return if (msgLength > maxWidth) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        }

        private val dp5 = DefaultSystemUtil.getPxFromDp(5f).toInt()

        private val toastLayout: ViewHolder?
            get() {
                val contentLayout = LinearLayout(DefaultBaseApplication.instance)
                contentLayout.setBackgroundResource(R.drawable.default_toast)
                val textView = TextView(DefaultBaseApplication.instance)
                textView.setShadowLayer(2.75f, 1f, 1f, Color.parseColor("#BB000000"))
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                @Suppress("DEPRECATION")
                textView.setTextColor(DefaultBaseApplication.instance.resources.getColor(android.R.color.background_light))
                textView.setPadding(dp5, dp5, dp5, dp5)
                contentLayout.addView(textView)
                return ViewHolder(contentLayout, textView)
            }
    }
}
