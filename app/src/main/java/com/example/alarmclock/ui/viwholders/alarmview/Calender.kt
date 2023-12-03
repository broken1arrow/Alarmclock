package com.example.alarmclock.ui.viwholders.alarmview

import android.content.res.ColorStateList
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat.getColor
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment

import com.example.alarmclock.R
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmSettings
import com.example.alarmclock.databinding.CalenderPopupMenuBinding
import com.google.android.material.button.MaterialButton
import org.threeten.bp.LocalDate

class Calender(private val button: MaterialButton,  alarmViewMenu: AlarmViewMenu) {
    private val fragment: Fragment = alarmViewMenu.homeFragment()

    val binding: CalenderPopupMenuBinding = CalenderPopupMenuBinding.inflate(
        LayoutInflater.from(button.context),  fragment.requireView() as ViewGroup?, false
    )

    private val popupWindow: PopupWindow = PopupWindow(
        binding.root,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        true
    )

    init {
        val background = binding.calenderBackground
        background.setBackgroundResource(R.drawable.background_round_corners)
        background.backgroundTintList =
            ColorStateList.valueOf(getColor(button.context, R.color.background_popup_window))

        // Set the background to null for a transparent background
        popupWindow.setBackgroundDrawable(null)
        // Set a desired animation style
        popupWindow.animationStyle = R.style.PopupAnimation
    }

    fun showPopup(anchorView: View) {
        // Show the popup at the specified location relative to the anchor view
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0)
    }

    fun closePopup() {
        popupWindow.dismiss()
    }

    fun onIntegration(alarmSettings: AlarmSettings){
        val isTextChangeByCode = false
        var date: LocalDate? = null
        binding.typeDateManually.doOnTextChanged { text, start, before, count ->
            date = formatDateFromString(isTextChangeByCode, text, date)
        }
        binding.calendarView.setOnDateChangeListener { viwe, year, month, dayOfMonth ->
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            date = selectedDate
        }
        binding.setDate.setOnClickListener {
            if (date != null) {
                alarmSettings.date = date as LocalDate
                    val formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern("dd-MMMM-yyyy")
                    val formattedDate = alarmSettings.date!!.format(formatter)
                button.text = formattedDate
            }
            closePopup()
        }
        binding.cancelButtonDate.setOnClickListener{
            closePopup()
        }
    }

    private fun formatDateFromString(
        isTextChangeByCode: Boolean,
        text: CharSequence?,
        date: LocalDate?
    ): LocalDate? {
        var isTextChangeByCode1 = isTextChangeByCode
        var date1 = date
        if (!isTextChangeByCode1) {
            val buildText = text?.let { StringBuilder(it) }
            if (text != null) {
                if (text.length == 4)
                    buildText?.append("-")
                if (text.length == 7)
                    buildText?.append("-")
                if (text.length == 4 || text.length == 7) {
                    isTextChangeByCode1 = true
                    binding.typeDateManually.setText(buildText)
                    binding.typeDateManually.setSelection(buildText?.length ?: 0)
                    isTextChangeByCode1 = false
                }
            }
        }
        if (text?.length == 10) {
            val splitText = text.split("-")
            if (splitText.isNotEmpty()) {
                date1 = LocalDate.of(splitText[0].toInt(), splitText[1].toInt(), splitText[2].toInt())
            }
        }
        return date1
    }

}