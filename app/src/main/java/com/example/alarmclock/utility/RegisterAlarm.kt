package com.example.alarmclock.utility

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import com.example.alarmclock.data.AlarmsUtility
import com.example.alarmclock.ui.viwholders.alarmview.AlarmReceiver
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmSettings
import com.google.android.material.snackbar.Snackbar
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.TemporalAdjusters
import java.util.Calendar
import kotlin.math.abs

class RegisterAlarm(private val context: Context, private var rootView: View?) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val zoneId: ZoneId = ZoneId.of("UTC")
    private val currentDay: LocalDateTime = LocalDateTime.now(zoneId)
    private val currentTime: LocalTime = LocalTime.now(zoneId)
    fun rescheduleAlarms() {
        val alarmsUtility = AlarmsUtility(context)
        val alarms = alarmsUtility.getAlarmsFromJson()
        alarms.forEach { alarm ->
            rescheduleAlarm(alarm)
        }
    }

    fun rescheduleAlarm(alarm: AlarmSettings) {
        // Check if the current day matches any of the specified days for the alarm
        val isMatchingDay = containsDay(alarm)
        val dateTime = LocalDateTime.of(
            alarm.date ?: currentDay.toLocalDate(),
            alarm.time ?: LocalTime.of(0, 0)
        )
        if (alarm.alarmOn && (isMatchingDay || (alarm.date != null && currentDay.isEqual(
                dateTime
            )))
        ) {
            val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                intent.putExtra("ALARM_ID", alarm.id)
                PendingIntent.getBroadcast(
                    context, alarm.id, intent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }
            val setDates: MutableList<LocalDateTime> = mutableListOf();
            if (alarm.days.isNotEmpty()) {
                calculateNextAlarmTime(setDates, alarm)
            } else {
                val dateSet = setCorrectDay(dateTime, alarm)
                setDates.add(dateSet)
            }

            if (setDates.isNotEmpty()) {
                val timeUntilAlarm = calculateTimeUntilAlarm(setDates.first())
                val snackbarMessage =
                    "Alarm set for ${timeUntilAlarm.days} days, ${timeUntilAlarm.hours} hours, and ${timeUntilAlarm.minutes} minutes from now."
                rootView?.let { Snackbar.make(it, snackbarMessage, Snackbar.LENGTH_LONG).show() }
            }
            for (scheduledDateTime in setDates) {
                val calendar = setDate(scheduledDateTime)
                setAlarmDate(isMatchingDay, calendar, alarmIntent)
            }
        }
    }

    private fun setAlarmDate(
        isMatchingDay: Boolean,
        calendar: Calendar,
        alarmIntent: PendingIntent
    ) {
        if (isMatchingDay) {

            if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.S) || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && alarmManager.canScheduleExactAlarms())) {
                alarmManager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(calendar.timeInMillis, alarmIntent),
                    alarmIntent
                )
            } else {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY * 7,
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

    private fun setDate(
        scheduledDateTime: LocalDateTime
    ): Calendar {
        val calendar = Calendar.getInstance()

        calendar.timeInMillis =
            scheduledDateTime.atZone(zoneId).toInstant().toEpochMilli()
        return calendar
    }

    private fun setCorrectDay(
        scheduledDateTime: LocalDateTime,
        alarm: AlarmSettings
    ): LocalDateTime {
        var newDateTime = scheduledDateTime
        if (currentDay.isAfter(scheduledDateTime) ||
            (currentDay.dayOfMonth == scheduledDateTime.dayOfMonth && alarm.time?.isBefore(
                scheduledDateTime.toLocalTime()
            ) == true)
        ) {
            newDateTime = scheduledDateTime.plusDays(1)
        }
        return newDateTime
    }

    private fun calculateTimeUntilAlarm(scheduledDateTime: LocalDateTime): TimeUntilAlarm {
        val now = LocalDateTime.now(zoneId)

        val days = (now.toLocalDate().dayOfMonth - scheduledDateTime.dayOfMonth).toLong()
        val hours: Long = if (scheduledDateTime.hour == 0) {
            (now.hour - (scheduledDateTime.hour + 24)).toLong()
        } else {
            (now.hour - scheduledDateTime.hour).toLong()
            // ChronoUnit.HOURS.between(now, scheduledDateTime)
        }
        val minutes: Long = if (scheduledDateTime.minute == 0) {
            (now.minute - (scheduledDateTime.minute + 60) % 60).toLong()
        } else {
            (now.minute - (scheduledDateTime.minute) % 60).toLong()
        }
        return TimeUntilAlarm(abs(days), abs(hours), abs(minutes))
    }

    data class TimeUntilAlarm(val days: Long, val hours: Long, val minutes: Long)

    private fun calculateNextAlarmTime(
        setDates: MutableList<LocalDateTime>,
        alarmSetting: AlarmSettings
    ) {
        for (nextAlarmDay in alarmSetting.days) {
            if (alarmSetting.time != null) {
                val setDate =
                    currentDay.with(TemporalAdjusters.next(DayOfWeek.valueOf(nextAlarmDay.name)));
                val day = LocalDateTime.of(setDate.toLocalDate(), alarmSetting.time)

                val dateSet = setCorrectDay(day, alarmSetting)
                setDates.add(dateSet)
            }
        }

    }

    private fun containsDay(
        alarm: AlarmSettings
    ): Boolean {
        if (alarm.days.isEmpty() && alarm.date == null)
            return true

        return true
    }
}