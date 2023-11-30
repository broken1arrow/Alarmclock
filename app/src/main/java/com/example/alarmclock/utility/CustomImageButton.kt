package com.example.alarmclock.utility

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.alarmclock.R

class CustomImageButton(var context: Context, private val button: ImageButton) {


    fun textOnButton(buttonParams : ViewGroup.LayoutParams, text: String): FrameLayout {

        val textView = TextView(context)
        textView.text = text
        textView.setTextColor(ContextCompat.getColor(context, R.color.text_default))

        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
        // Create a FrameLayout to overlay the TextView on the ImageButton
        val frameLayout = FrameLayout(context)
        frameLayout.layoutParams = buttonParams
        frameLayout.addView(button, params)
        frameLayout.addView(textView, params)
        return frameLayout
    }

    fun animateOnClick(it: View) {
        it.animate().apply {
            scaleX(1.1f)
            scaleY(1.1f)
            duration = 200
        }.withEndAction {
            it.animate().scaleX(1.0f).scaleY(1.0f).duration = 200
        }
    }
}