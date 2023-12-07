package com.example.alarmclock.settings

data class GeneralSettings(
    var snoozeTime: Long = 300,
    var startWithMonday: Boolean = true
)