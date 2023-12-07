package com.example.alarmclock.settings

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SettingsUtility(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun saveSettingToJson(generalSettings: GeneralSettings) {
        val gson = Gson()
        val json = gson.toJson(generalSettings)
        sharedPreferences.edit().putString("settings_data", json).apply()
    }

    fun getSettingFromJson(): GeneralSettings {
        val json = sharedPreferences.getString("settings_data", null)
        return if (!json.isNullOrBlank()) {
            val gson = Gson()
            gson.fromJson(json, GeneralSettings::class.java)
        } else {
            GeneralSettings()
        }
    }
}