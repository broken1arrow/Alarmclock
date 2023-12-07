package com.example.alarmclock.ui.viwholders.alarmview


import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.alarmclock.R
import com.example.alarmclock.databinding.SetSnoozeTimeBinding
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmSettings
import com.google.android.material.button.MaterialButton
import org.threeten.bp.LocalTime

class SetSnoozeTime(private val button: MaterialButton, alarmViewMenu: AlarmViewMenu) {

    private val fragment: Fragment = alarmViewMenu.homeFragment()
    private val list: MutableList<Int> = mutableListOf()


    val binding: SetSnoozeTimeBinding = SetSnoozeTimeBinding.inflate(
        LayoutInflater.from(button.context), fragment.requireView() as ViewGroup?, false
    )

    private val popupWindow: PopupWindow = PopupWindow(
        binding.root,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        true
    )

    init {
        val background = binding.snoozeBackground
        background.setBackgroundResource(R.drawable.background_round_corners)
        background.backgroundTintList =
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    button.context,
                    R.color.background_popup_window
                )
            )

        // Set the background to null for a transparent background
        popupWindow.setBackgroundDrawable(null)
        // Set a desired animation style
        popupWindow.animationStyle = R.style.PopupAnimation
        for (time in 0..59) {
            list.add(time);
        }
        val arrayAdapter =
            fragment.context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_item, list) }
        arrayAdapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.hours.adapter = arrayAdapter
        binding.minutes.adapter = arrayAdapter
        binding.seconds.adapter = arrayAdapter
    }

    fun showPopup(anchorView: View) {
        // Show the popup at the specified location relative to the anchor view
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)
    }

    fun closePopup() {
        popupWindow.dismiss()
    }

    fun onIntegration(alarmSettings: AlarmSettings) {

        var hour = 0
        var minutes = 0
        var seconds = 0

        binding.hours.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                hour = list[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        binding.minutes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                minutes = list[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.seconds.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                seconds = list[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        binding.applayTime.setOnClickListener {
            var secondsSet = 0;

            if (hour > 0) secondsSet += hour * (60 * 60)
            if (minutes > 0) secondsSet += minutes * 60
            if (seconds > 0) secondsSet += seconds

            if (secondsSet > 0) {
                alarmSettings.snoozeTime = secondsSet.toLong()
                button.text =  button.context.getString(R.string.snooze_set,formatSnoozeTime(secondsSet))
            } else
                button.text = button.context.getString(R.string.snooze_time)

            closePopup()
        }

        binding.cancel.setOnClickListener {
            closePopup()
        }
    }

    private fun formatSnoozeTime(seconds: Int): String {
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
