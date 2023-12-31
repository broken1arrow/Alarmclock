package com.example.alarmclock.ui.viwholders.alarmview.cache

import com.example.alarmclock.ui.viwholders.alarmview.Mode
import com.example.alarmclock.utility.Days
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime


data class AlarmSettings(
    var id: Int = 0,
    var snoozeTime : Long = -1,
    var date: LocalDate? = null,
    var time: LocalTime? = null,
    var alarmOn: Boolean = false,
    var days: MutableList< Days> = mutableListOf(),
    var vibrate: Boolean= false,
    var expand : Mode = Mode.NORMAL
)