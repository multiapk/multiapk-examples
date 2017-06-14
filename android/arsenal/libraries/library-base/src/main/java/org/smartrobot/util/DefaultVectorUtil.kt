package org.smartrobot.util

import android.graphics.drawable.Drawable
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.support.v7.widget.AppCompatDrawableManager
import org.smartrobot.base.DefaultBaseApplication

object DefaultVectorUtil {
    fun getVectorDrawableCompat(vectorResId: Int): Drawable {
        return AppCompatDrawableManager.get().getDrawable(DefaultBaseApplication.instance, vectorResId)
    }

    fun getAnimatedVectorDrawableCompat(animatedVectorResId: Int): AnimatedVectorDrawableCompat? {
        return AnimatedVectorDrawableCompat.create(DefaultBaseApplication.instance, animatedVectorResId)
    }
}