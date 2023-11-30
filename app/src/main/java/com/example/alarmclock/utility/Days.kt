package com.example.alarmclock.utility

import com.example.alarmclock.R

enum class Days(val day: Int, val firstLetters: Int) {

    MONDAY(R.string.monday, R.string.mo),
    TUESDAY(R.string.tuesday, R.string.tu),
    WEDNESDAY(R.string.wednesday, R.string.we),
    THURSDAY(R.string.thursday, R.string.th),
    FRIDAY(R.string.friday, R.string.fr),
    SATURDAY(R.string.saturday, R.string.sa),
    SUNDAY(R.string.sunday, R.string.su);

}