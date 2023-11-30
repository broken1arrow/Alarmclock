package com.example.alarmclock.ui.viwholders.alarmview.utility

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat.getColor
import com.example.alarmclock.R
import com.example.alarmclock.databinding.AlarmSnozzeMenuBinding

class SnozzeAlarmTime(private val context: Context) {

    val binding: AlarmSnozzeMenuBinding = AlarmSnozzeMenuBinding.inflate(
        LayoutInflater.from(context), null, false
    )

    private val popupWindow: PopupWindow = PopupWindow(
        binding.root,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        true
    )

    init {
        val background = binding.textView2
        background.setBackgroundResource(R.drawable.background_round_corners)
        background.backgroundTintList =
            ColorStateList.valueOf(getColor(context, R.color.background_popup_window))

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

    fun onIntegration() {
    }
}