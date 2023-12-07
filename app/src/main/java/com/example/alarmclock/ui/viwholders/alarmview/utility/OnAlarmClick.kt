package com.example.alarmclock.ui.viwholders.alarmview.utility

import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.TableRow
import androidx.core.content.ContextCompat.getString
import com.example.alarmclock.R
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmSettings
import com.example.alarmclock.databinding.AlarmMenuBinding
import com.example.alarmclock.ui.viwholders.alarmview.AlarmViewMenu
import com.example.alarmclock.ui.viwholders.alarmview.Calender
import com.example.alarmclock.ui.viwholders.alarmview.Mode
import com.example.alarmclock.ui.viwholders.alarmview.SetAlarmTime
import com.example.alarmclock.ui.viwholders.alarmview.SetSnoozeTime
import com.example.alarmclock.utility.CustomImageButton
import com.example.alarmclock.utility.Days
import com.example.alarmclock.utility.RegisterAlarm
import com.example.alarmclock.utility.Shake


class OnAlarmClick(
    private val menuBinding: AlarmMenuBinding,
    private val alarmViewMenu: AlarmViewMenu,
    private val alarmSettings: AlarmSettings,
    private val position: Int
) {


    init {
        onSetExpandClick()
        onSetTimeClick()
        onSetSwitchClick()
        onSetDayClick()
        onSetDateClick()
        onSetVibrationClick()
        onSetAlarmSoundClick()

        onSetDeleteClick()
        onSetSnoozeTime()
    }

    fun onSetExpandClick() {
        menuBinding.expandedView.visibility = View.GONE
        alarmSettings.expand = Mode.NORMAL
        // if (alarmSettings.expand != Mode.NORMAL) View.VISIBLE else View.GONE
        menuBinding.toggleExtendedView.setOnClickListener {
            if (alarmSettings.expand == Mode.EXTENDED) menuBinding.toggleExtendedView.setImageResource(
                R.drawable.ic_down_arrow
            )
            else menuBinding.toggleExtendedView.setImageResource(R.drawable.ic_up_arrow)

            alarmSettings.expand =
                if (alarmSettings.expand == Mode.NORMAL) Mode.EXTENDED else Mode.NORMAL
            menuBinding.expandedView.visibility =
                if (alarmSettings.expand != Mode.NORMAL) View.VISIBLE else View.GONE
            // alarmViewMenu.updateMenu(position);
        }
    }

    fun onSetTimeClick() {
        menuBinding.alarmTime.text =
            if (alarmSettings.time != null) {
                setTime()
            } else getString(menuBinding.alarmTime.context, R.string.time_default)

        menuBinding.alarmTime.setOnClickListener {
            val setAlarmTime = SetAlarmTime(menuBinding.alarmTime, alarmViewMenu)
            menuBinding
            setAlarmTime.showPopup(it)
            setAlarmTime.onIntegration(alarmSettings)
        }
    }


    fun onSetSwitchClick() {
        menuBinding.switch1?.isChecked = alarmSettings.alarmOn
        menuBinding.switch1?.setOnClickListener {
            alarmSettings.alarmOn = menuBinding.switch1.isChecked
            val register = RegisterAlarm(menuBinding.switch1.context)
            register.rescheduleAlarm(alarmSettings)
        }
    }

    fun onSetDayClick() {

        val dayList = menuBinding.daysList
        val buttonParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT
        )
        buttonParams.weight = 1F
        val tableRow = TableRow(dayList.context)
        for (i in Days.values()) {
            val (button, imageButton) = createImageButton(dayList, i, buttonParams, tableRow)
            if (alarmSettings.days.contains(i)) {
                button.setImageResource(R.drawable.empty_day_pressed)
            }

            button.setOnClickListener {
                imageButton.animateOnClick(it)
                val day = Days.entries[button.id]
                if (alarmSettings.days.isEmpty() || !alarmSettings.days.contains(day)) {
                    button.setImageResource(R.drawable.empty_day_pressed)
                    alarmSettings.days.add(day)
                } else {
                    //button.imageTintList = null
                    button.setImageResource(R.drawable.empty_day)
                    alarmSettings.days.remove(day)
                }
                setDays(dayList.context)
            }
        }
        setDays(dayList.context)

        dayList.addView(tableRow)
    }

    fun onSetDateClick() {
        val dateForAlarm = menuBinding.setDateForAlarm
        if (alarmSettings.date != null) {
            val formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
            val formattedDate = alarmSettings.date!!.format(formatter)
            dateForAlarm.text = formattedDate
        }
        dateForAlarm.setOnClickListener {
            val calender = Calender(dateForAlarm, alarmViewMenu)
            calender.showPopup(it)
            calender.onIntegration(alarmSettings)
        }
    }

    fun onSetVibrationClick() {
        menuBinding.setAlarmVibration.setOnClickListener {
            Shake.shake(it.context)
            alarmSettings.vibrate = !alarmSettings.vibrate

            //menuBinding.setAlarmVibration.context
        }
    }

    fun onSetAlarmSoundClick() {
        menuBinding.setAlarmSound.setOnClickListener {

        }
    }

    fun onSetDeleteClick() {
        val removeAlarm = menuBinding.removeAlarm
        removeAlarm.setOnClickListener {

            alarmViewMenu.removeItem(position)
        }
    }

    fun onSetSnoozeTime() {
        if (alarmSettings.snoozeTime > 0)
            menuBinding.snoozeTime.text = menuBinding.snoozeTime.context
                .getString(R.string.snooze_set, formatSnoozeTime(alarmSettings.snoozeTime))

        menuBinding.snoozeTime.setOnClickListener {
            var snoozeTime = SetSnoozeTime(menuBinding.snoozeTime, alarmViewMenu)
            snoozeTime.showPopup(it)
            snoozeTime.onIntegration(alarmSettings)
        }

    }

    private fun createImageButton(
        dayList: TableRow, day: Days, buttonParams: TableRow.LayoutParams, tableRow: TableRow
    ): Pair<ImageButton, CustomImageButton> {
        val button = ImageButton(dayList.context)
        button.id = day.ordinal
        button.setBackgroundResource(R.drawable.day_background)
        button.setImageResource(R.drawable.empty_day)

        val imageButton = CustomImageButton(dayList.context, button)
        val frameLayout = imageButton.textOnButton(
            buttonParams, dayList.context.getString(day.firstLetters)
        )
        tableRow.addView(frameLayout)
        return Pair(button, imageButton)
    }

    private fun setDays(context: Context) {
        val builder = StringBuilder()
        val listOfDays = alarmSettings.days
        for (day in listOfDays) {
            builder.append(context.getString(day.day))
            if (listOfDays.size > 1)
                builder.append(", ")
        }
        if (listOfDays.size > 1)
            builder.setLength(builder.length - 2)
        if (builder.isNotEmpty())
            menuBinding.scheduledAlarms.text = builder
        else {
            menuBinding.scheduledAlarms.text =
                builder.append(context.getString(R.string.no_alarm_scheduled))
        }
    }

    private fun setTime(): String {
        val hour = alarmSettings.time!!.hour
        val minute = alarmSettings.time!!.minute
        var hourFormatted = hour.toString()
        var minuteFormatted = minute.toString()
        if (hour < 10)
            hourFormatted = "0$hourFormatted"
        if (minute < 10)
            minuteFormatted = "0$minuteFormatted"
        return menuBinding.alarmTime.context.getString(
            R.string.alarm_time_with_placeholders,
            hourFormatted,
            minuteFormatted
        )
    }

    private fun formatSnoozeTime(seconds: Long): String {
        val hours = seconds / 3600
        val remainingSeconds = seconds % 3600
        val minutes = remainingSeconds / 60
        val secs = remainingSeconds % 60

        val formattedTime = StringBuilder()

        if (hours > 0) {
            if (hours < 10)
                formattedTime.append(String.format("%01d", hours))
            else
                formattedTime.append(String.format("%02d", hours))
            formattedTime.append(" hours ")
        }

        if (minutes > 0 || hours > 0) {
            if (minutes < 10)
                formattedTime.append(String.format("%01d", minutes))
            else
                formattedTime.append(String.format("%02d", minutes))
            formattedTime.append(" min ")
        }
        if (secs > 0) {
            if (secs < 10)
                formattedTime.append(String.format("%01d", secs))
            else
                formattedTime.append(String.format("%02d", secs))
            formattedTime.append(" sec")
        }

        return formattedTime.toString()
    }

}