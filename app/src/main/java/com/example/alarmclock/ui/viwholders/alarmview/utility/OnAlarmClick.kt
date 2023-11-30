package com.example.alarmclock.ui.viwholders.alarmview.utility

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.ImageButton
import android.widget.TableRow
import com.example.alarmclock.R
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmSettings
import com.example.alarmclock.databinding.AlarmMenuBinding
import com.example.alarmclock.ui.viwholders.alarmview.AlarmViewMenu
import com.example.alarmclock.ui.viwholders.alarmview.Calender
import com.example.alarmclock.ui.viwholders.alarmview.Mode
import com.example.alarmclock.ui.viwholders.alarmview.SetAlarmTime
import com.example.alarmclock.utility.CustomImageButton
import com.example.alarmclock.utility.Days
import com.example.alarmclock.utility.RegisterAlarm


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
    }

    fun onSetExpandClick() {
        menuBinding.expandedView.visibility =
            if (alarmSettings.expand != Mode.NORMAL) View.VISIBLE else View.GONE
        menuBinding.toggleExtendedView.setOnClickListener {
            if (alarmSettings.expand == Mode.EXTENDED)
                menuBinding.toggleExtendedView.setImageResource(R.drawable.ic_down_arrow)
            else
                menuBinding.toggleExtendedView.setImageResource(R.drawable.ic_up_arrow)

            alarmSettings.expand =
                if (alarmSettings.expand == Mode.NORMAL) Mode.EXTENDED else Mode.NORMAL
            alarmViewMenu.updateMenu(position);
        }
    }

    fun onSetTimeClick() {
        menuBinding.alarmTime.setOnClickListener {
            val setAlarmTime = SetAlarmTime(menuBinding.alarmTime, alarmViewMenu)
            menuBinding
            setAlarmTime.showPopup(it)
            setAlarmTime.onIntegration(alarmSettings)
        }
    }

    fun onSetSwitchClick() {
        menuBinding.switch1.isChecked = alarmSettings.alarmOn
        menuBinding.switch1.setOnClickListener {
            alarmSettings.alarmOn = menuBinding.switch1.isChecked
            val register = RegisterAlarm(menuBinding.switch1.context)
            register.rescheduleAlarm(alarmSettings)
        }
    }

    fun onSetDayClick() {

        val dayList = menuBinding.daysList
        val buttonParams = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT
        )
        buttonParams.weight = 1F
        val tableRow = TableRow(dayList.context)

        for (i in Days.values()) {
            val (button, imageButton) = createImageButton(dayList, i, buttonParams, tableRow)
            if (alarmSettings.days.contains(i))
                button.setImageResource(R.drawable.empty_day_pressed)

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
            }
        }
        dayList.addView(tableRow)
    }

    fun onSetDateClick() {
        val dateForAlarm = menuBinding.setDateForAlarm
        dateForAlarm.setOnClickListener {
            val calender = Calender(dateForAlarm.context, alarmViewMenu)
            calender.showPopup(it)
            calender.onIntegration(alarmSettings)
        }
    }

    fun onSetVibrationClick() {
        menuBinding.setAlarmVibration.setOnClickListener {
            shakeItBaby(it.context)
            alarmSettings.vibrate = !alarmSettings.vibrate

            //menuBinding.setAlarmVibration.context
        }
    }

    fun onSetAlarmSoundClick() {
        menuBinding.setAlarmSound.setOnClickListener {

        }
    }

    fun onSetAddClick() {
        alarmViewMenu.homeFragment().binding.addAlarm.setOnClickListener {
            alarmViewMenu.addItem(position)
        }
    }

    fun onSetDeleteClick() {
        val removeAlarm = menuBinding.removeAlarm
        removeAlarm.setOnClickListener {
            alarmViewMenu.removeItem(position)
        }
    }

    private fun createImageButton(
        dayList: TableRow,
        day: Days,
        buttonParams: TableRow.LayoutParams,
        tableRow: TableRow
    ): Pair<ImageButton, CustomImageButton> {
        val button = ImageButton(dayList.context)
        button.id = day.ordinal
        button.setBackgroundResource(R.drawable.day_background)
        button.setImageResource(R.drawable.empty_day)

        val imageButton = CustomImageButton(dayList.context, button)
        val frameLayout = imageButton.textOnButton(
            buttonParams,
            dayList.context.getString(day.firstLetters)
        )
        tableRow.addView(frameLayout)
        return Pair(button, imageButton)
    }

    private fun shakeItBaby(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            (context.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(
                VibrationEffect.createOneShot(
                    150,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            (context.getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(150)
        }
    }
}