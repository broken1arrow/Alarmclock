package com.example.alarmclock.ui.viwholders.alarmview

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.example.alarmclock.data.AlarmsUtility
import com.example.alarmclock.settings.SettingsUtility
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmSettings

class AlarmReceiver : BroadcastReceiver() {
    private val handler = Handler(Looper.getMainLooper())

    override fun onReceive(context: Context, intent: Intent) {
        println("alarm goo off")
        /*        // Inflate the menu
                // Create an Intent to open the AlarmSnooze activity when the notification is clicked
                val alarmSnoozeIntent = Intent(context, AlarmSnooze::class.java)
                val pendingIntent = PendingIntent.getActivity(
                    context, 0, alarmSnoozeIntent, PendingIntent.FLAG_IMMUTABLE
                )*/
        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        val snoozePendingAction = snoozeAlarm(context)
        val turnOffPendingAction = turnOffAlarm(context)

        if (intent.action.equals("SNOOZE_ACTION")) {
            ringtone?.stop()
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(NOTIFICATION_ID)
            val alarm = currentAlarmList!!.find { it.id == alarmId }
            val snoozeTime = if (alarm == null || alarm.snoozeTime == -1L) {
                val settingsUtility = SettingsUtility(context)
                settingsUtility.getSettingFromJson().snoozeTime
            } else {
                alarm.snoozeTime
            }
            handler.postDelayed({
                playSound(context)
                setNotification(context, snoozePendingAction, turnOffPendingAction)
            }, 1000 * snoozeTime)
            return
        }
        if (intent.action.equals("TURN_OFF_ACTION")) {
            ringtone?.stop()
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(NOTIFICATION_ID)
            if (currentAlarmList != null) {
                val alarm = currentAlarmList!!.find { it.id == alarmId }
                if (!alarm?.days.isNullOrEmpty()) {
                    alarm?.alarmOn = false
                    alarmsUtility?.saveAlarmsToJson(currentAlarmList!!)
                }
                clearAlarm()
            }
            return
        }

        if (currentAlarmList == null)
            getAlarmList(context, alarmId)
        else {
            clearAlarm()
            getAlarmList(context, alarmId)
        }

        setNotification(context, snoozePendingAction, turnOffPendingAction)
        playSound(context)
    }

    private fun setNotification(
        context: Context,
        snoozePendingAction: PendingIntent?,
        turnOffPendingAction: PendingIntent?
    ) {
        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_lock_idle_alarm)
            .setContentTitle(context.getString(com.example.alarmclock.R.string.alarm_notifcation))
            .setContentText(context.getString(com.example.alarmclock.R.string.text_notification))
            .addAction(
                R.drawable.ic_lock_silent_mode,
                context.getString(com.example.alarmclock.R.string.snooze_button_notification),
                snoozePendingAction
            )
            .addAction(
                R.drawable.ic_delete,
                context.getString(com.example.alarmclock.R.string.turn_off_button_notification),
                turnOffPendingAction
            )
            .setAutoCancel(true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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

    private fun snoozeAlarm(context: Context): PendingIntent? {
        val snoozeIntent = Intent(context, AlarmReceiver::class.java)
        snoozeIntent.action = "SNOOZE_ACTION"
        return PendingIntent.getBroadcast(
            context, 0, snoozeIntent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun turnOffAlarm(context: Context): PendingIntent? {
        val turnOffIntent = Intent(context, AlarmReceiver::class.java)
        turnOffIntent.action = "TURN_OFF_ACTION"

        return PendingIntent.getBroadcast(
            context, 0, turnOffIntent, PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun playSound(context: Context) {
        val notificationSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, notificationSoundUri)
        handler.postDelayed({
            if (ringtone?.isPlaying == true)
                return@postDelayed
            ringtone?.play()
        }, 50)
        /*      handler.postDelayed({
                  ringtone?.stop()
              }, 20000)*/
    }

    private fun rescheduleAlarms(context: Context) {
    }

    fun startAlarm(context: Context) {
        playSound(context)

    }

    companion object {
        private const val CHANNEL_ID = "your_channel_id"
        private const val NOTIFICATION_ID = 1
        private var ringtone: Ringtone? = null
        private var currentAlarmList: MutableList<AlarmSettings>? = null
        private var alarmsUtility: AlarmsUtility? = null
        private fun getAlarmList(context: Context, alarmId: Int) {
            alarmsUtility = AlarmsUtility(context)
            currentAlarmList = alarmsUtility!!.getAlarmsFromJson().toMutableList()
        }

        private fun clearAlarm() {
            alarmsUtility = null
            currentAlarmList = null
        }
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