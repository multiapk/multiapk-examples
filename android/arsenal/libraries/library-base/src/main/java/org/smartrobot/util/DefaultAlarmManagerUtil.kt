package org.smartrobot.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context

import org.smartrobot.base.DefaultBaseApplication


object DefaultAlarmManagerUtil {

    /*
        Intent intent = new Intent(MAlarmBroadcastReceiver.ACTION);//必须静态注册
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mActivity, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        MAlarmManagerUtil.setNormalAlarm(calendar.getTimeInMillis(), pendingIntent);
     */
    fun setNormalAlarm(timeInMillis: Long, pendingIntent: PendingIntent): PendingIntent {
        val context = DefaultBaseApplication.instance.applicationContext
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        return pendingIntent
    }

    fun setRepeatAlarm(timeInMillis: Long, intervalMillis: Long, pendingIntent: PendingIntent): PendingIntent {
        val context = DefaultBaseApplication.instance.applicationContext
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, intervalMillis, pendingIntent)
        return pendingIntent
    }

    fun cancelAlarm(pendingIntent: PendingIntent) {
        val context = DefaultBaseApplication.instance.applicationContext
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}
