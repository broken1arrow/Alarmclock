package com.example.alarmclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.alarmclock.data.AlarmsUtility
import com.example.alarmclock.ui.viwholders.alarmview.AlarmReceiver
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmSettings
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.Calendar

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            rescheduleAlarms(context)
        }
    }

    private fun rescheduleAlarms(context: Context) {
        val alarmsUtility = AlarmsUtility(context)

        val alarms = alarmsUtility.getAlarmsFromJson()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val currentDay = LocalDate.now(ZoneId.systemDefault())

        alarms.forEach { alarm ->
            // Check if the current day matches any of the specified days for the alarm
            val isMatchingDay = containsDay(alarm, currentDay.dayOfWeek)
            if (alarm.alarmOn && (isMatchingDay || currentDay.isEqual(alarm.date))) {
                val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                    PendingIntent.getBroadcast(
                        context, alarm.id, intent,
                        PendingIntent.FLAG_IMMUTABLE
                    )
                }

                // Use the alarm's scheduled date and time
                val calendar = Calendar.getInstance()
                val scheduledDateTime = LocalDateTime.of(currentDay, alarm.time)
                calendar.timeInMillis =
                    scheduledDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
                if (isMatchingDay) {     // Schedule a repeating alarm on the specified days of the week
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY * 7,  // Repeat every week
                        alarmIntent
                    )
                } else {
                    alarmManager.set(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        alarmIntent
                    )
                }
            }
        }

        //  alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, alarmIntent)
    }

    private fun containsDay(
        alarm: AlarmSettings,
        currentDayOfWeek: DayOfWeek
    ): Boolean {
        return alarm.days.any {
            it.name == currentDayOfWeek.name
        }
    }
}