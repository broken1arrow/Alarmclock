package com.example.alarmclock.ui.viwholders.alarmview

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.ContextThemeWrapper
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.NotificationCompat
import com.example.alarmclock.AlarmSnooze
import com.example.alarmclock.R
import com.example.alarmclock.ui.viwholders.alarmview.utility.SnozzeAlarmTime
import com.google.android.material.snackbar.Snackbar

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        println("alarm goo off")
        // Inflate the menu
        // Create an Intent to open the AlarmSnooze activity when the notification is clicked
        val alarmSnoozeIntent = Intent(context, AlarmSnooze::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, alarmSnoozeIntent, PendingIntent.FLAG_IMMUTABLE
        )
        // Create an Intent for the snooze action
        val snoozeIntent = Intent(context, AlarmReceiver::class.java)
        snoozeIntent.action = "SNOOZE_ACTION"
        val snoozePendingAction = PendingIntent.getBroadcast(
            context, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE
        )

        // Create an Intent for the turn off action
        val turnOffIntent = Intent(context, AlarmReceiver::class.java)
        turnOffIntent.action = "TURN_OFF_ACTION"
        val turnOffPendingAction = PendingIntent.getBroadcast(
            context, 0, turnOffIntent, PendingIntent.FLAG_IMMUTABLE
        )

        // Create the notification
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Alarm Notification")
            .setContentText("Time to snooze!")
            .addAction(android.R.drawable.ic_lock_silent_mode, "Snooze", snoozePendingAction)
            .addAction(android.R.drawable.ic_delete, "Turn Off", turnOffPendingAction)
            //.setContentIntent(pendingIntent)
            .setAutoCancel(true) // Automatically remove the notification when clicked

        // Show the notification
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check if Android version is Oreo or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarm Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Show the notification
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun playSound(context: Context) {

    }

    private fun rescheduleAlarms(context: Context) {
    }

    companion object {
        private const val CHANNEL_ID = "your_channel_id"
        private const val NOTIFICATION_ID = 1
    }
    /*        val snoozeIntent = Intent(context, xc::class.java)
        val snoozePendingIntent = PendingIntent.getActivity(
            context, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Create a Notification with actions
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Alarm")
            .setContentText("It's time to wake up!")
            //.setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(snoozePendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
      *//*      .addAction(
                R.drawable.ic_snooze,
                "Snooze",
                snoozePendingIntent
            )*//*
            .build()

        // Display the notification
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)*/
}