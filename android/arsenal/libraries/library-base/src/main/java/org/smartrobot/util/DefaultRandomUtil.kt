package org.smartrobot.util

import android.graphics.Color

import java.util.Locale

object DefaultRandomUtil {
    //start>0 end>0
    fun getRandom(begin: Int, end: Int): Int {
        val absBegin = Math.abs(begin)
        val absEnd = Math.abs(end)

        return (Math.random() * Math.abs(absBegin - absEnd) + if (absBegin > absEnd) absEnd else absBegin).toInt()
    }

    val randomTransparentAvatar: String
        get() = String.format(Locale.getDefault(), "http://odw6aoxik.bkt.clouddn.com/avatar_tranparent_%d.png-200x200", DefaultRandomUtil.getRandom(1, 15))

    val randomCartoonAvatar: String
        get() = String.format(Locale.getDefault(), "http://odw6aoxik.bkt.clouddn.com/avatar_cartoon_%d.jpg-200x200", DefaultRandomUtil.getRandom(1, 50))

    //－16777216
    //-1
    val randomColor: Int
        get() {
            val color00 = java.lang.Long.parseLong("ff000000", 16).toInt()
            val colorFF = java.lang.Long.parseLong("ffffffff", 16).toInt()
            val hexColorStr = Integer.toHexString((-(Math.random() * (Math.abs(color00) - Math.abs(colorFF)) + Math.abs(colorFF))).toInt())
            return Color.parseColor(String.format("#%s", hexColorStr))
        }
}
