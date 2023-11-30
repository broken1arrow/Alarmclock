package com.example.alarmclock.utility

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.alarmclock.data.AlarmsUtility
import com.example.alarmclock.ui.viwholders.alarmview.AlarmReceiver
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmSettings
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.Calendar

class RegisterAlarm(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val currentDay: LocalDate = LocalDate.now(ZoneId.of("UTC"))

    fun rescheduleAlarms() {
        val alarmsUtility = AlarmsUtility(context)
        val alarms = alarmsUtility.getAlarmsFromJson()
        alarms.forEach { alarm ->
            rescheduleAlarm(alarm)
        }
    }

    fun rescheduleAlarm(alarm: AlarmSettings) {
        // Check if the current day matches any of the specified days for the alarm
        val isMatchingDay = containsDay(alarm, currentDay.dayOfWeek)


        if (alarm.alarmOn && (isMatchingDay || (alarm.date != null && currentDay.isEqual(alarm.date)))) {
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
                scheduledDateTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli()
            //
            if (isMatchingDay) {     // Schedule a repeating alarm on the specified days of the week
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms() && alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setAlarmClock(
                        AlarmManager.AlarmClockInfo(calendar.timeInMillis, alarmIntent),
                        alarmIntent
                    )
                } else {
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY * 7,  // Repeat every week
                        alarmIntent
                    )
                }
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    alarmIntent
                )
            }
        }
    }

    private fun containsDay(
        alarm: AlarmSettings,
        currentDayOfWeek: DayOfWeek
    ): Boolean {
        if (alarm.days.isEmpty() && alarm.date == null)
            return true

        return alarm.days.any {
            it.name == currentDayOfWeek.name
        }
    }
}