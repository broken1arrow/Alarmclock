package com.example.alarmclock.ui.viwholders.alarmview

import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment

import com.example.alarmclock.R
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmSettings
import com.example.alarmclock.databinding.SetAlarmTimeBinding
import org.threeten.bp.LocalTime

class SetAlarmTime(private val view: TextView, alarmViewMenu: AlarmViewMenu) {
    private val fragment: Fragment = alarmViewMenu.homeFragment()

    val binding: SetAlarmTimeBinding = SetAlarmTimeBinding.inflate(
        LayoutInflater.from(view.context), fragment.requireView() as ViewGroup?, false
    )

    private val popupWindow: PopupWindow = PopupWindow(
        binding.root,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        true
    )

    init {
        val background = binding.alarmBackground
        background.setBackgroundResource(R.drawable.background_round_corners)
        background.backgroundTintList =
            ColorStateList.valueOf(getColor(view.context, R.color.background_popup_window))

        // Set the background to null for a transparent background
        popupWindow.setBackgroundDrawable(null)
        // Set a desired animation style
        popupWindow.animationStyle = R.style.PopupAnimation
    }

    fun showPopup(anchorView: View) {
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)
    }

    fun closePopup() {
        popupWindow.dismiss()
    }

    fun onIntegration(alarmSettings: AlarmSettings) {
        var minute = -1
        var hour = -1
        binding.setHour.setSelection(0)
        binding.setMinute.setSelection(0)

        binding.setHour.doOnTextChanged { text, start, before, count ->
            if (text?.isNotEmpty() == true) {
                val hours = text.toString().toIntOrNull()
                if (hours != null) {
                    var hoursChecked = hours
                    if (hours > 23) {
                        val textSub = text.subSequence(0, text.length - 1).toString()
                        binding.setHour.setText(textSub)
                        binding.setHour.setSelection(text.length - 1)
                        hoursChecked = textSub.toInt()

                    }
                    hour = hoursChecked
                } else {
                    binding.setHour.text = null
                }
            }
        }
        binding.setMinute.doOnTextChanged { text, start, before, count ->
            if (text?.isNotEmpty() == true) {
                val minutes = text.toString().toIntOrNull()
                if (minutes != null) {
                    var minutesChecked = minutes
                    if (minutes > 59) {
                        val textSub = text.subSequence(0, text.length - 1).toString()
                        binding.setMinute.setText(textSub)
                        binding.setMinute.setSelection(text.length - 1)
                        minutesChecked = textSub.toInt()
                    }
                    minute = minutesChecked
                } else {
                    binding.setMinute.text = null
                }
            }
        }

        binding.applayTime.setOnClickListener {
            if (hour >= 0 && minute >= 0) {
                var hourFormatted = hour.toString()
                var minuteFormatted = minute.toString()
                if (hour < 10)
                    hourFormatted = "0$hourFormatted"
                if (minute < 10)
                    minuteFormatted = "0$minuteFormatted"

                alarmSettings.time = LocalTime.of(hour, minute)
                view.text = view.context.getString(
                    R.string.alarm_time_with_placeholders,
                    hourFormatted,
                    minuteFormatted
                )
            }
            closePopup()

        }
        binding.cancel.setOnClickListener {
            closePopup()
        }
    }
}