package com.example.alarmclock.data

import android.content.Context
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AlarmsUtility(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("alarms", Context.MODE_PRIVATE)

    fun saveAlarmsToJson(alarmsList: List<AlarmSettings>) {
        val gson = Gson()
        val json = gson.toJson(alarmsList)
        sharedPreferences.edit().putString("alarms_data", json).apply()
    }

    fun getAlarmsFromJson(): List<AlarmSettings> {
        val json = sharedPreferences.getString("alarms_data", null)
        return if (!json.isNullOrBlank()) {
            val gson = Gson()
            gson.fromJson(json, object : TypeToken<List<AlarmSettings>>() {}.type)
        } else {
            val list = mutableListOf<AlarmSettings>()
            list.add(AlarmSettings())
            list
            //emptyList()
        }
    }
}