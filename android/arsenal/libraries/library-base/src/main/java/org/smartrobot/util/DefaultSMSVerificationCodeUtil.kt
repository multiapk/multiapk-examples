package org.smartrobot.util

import android.widget.TextView

import java.util.Timer
import java.util.TimerTask

class DefaultSMSVerificationCodeUtil {

    interface OnCallBack {
        fun onTimeOver()

        fun onEverySecond(secondsLeft: Int)
    }

    private var second = 0
    private val onCallBack: OnCallBack? = null
    private val textView: TextView? = null
    private var timer: Timer? = null

    fun onAfterSuccessRequest(allSeconds: Int, onCallBack: OnCallBack) {
        second = 0
        forceStop()
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {
                if (second >= allSeconds)
                    onCallBack.onTimeOver()
                else
                    onCallBack.onEverySecond(allSeconds - ++second)
            }
        }, 0, 1000)
    }

    fun forceStop() {
        if (timer != null) {
            timer!!.purge()
            timer!!.cancel()
            timer = null
        }
    }
}