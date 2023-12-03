package com.example.alarmclock.utility

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

object Shake {

   fun shake(context: Context) {
        if (Build.VERSION.SDK_INT >= 26) {
            (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(
                VibrationEffect.createOneShot(
                    150, VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(150)
        }
    }
}