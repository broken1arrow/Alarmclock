package com.example.alarmclock.ui.viwholders.alarmview.cache

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.alarmclock.data.AlarmsUtility

class AlarmViewModel(app: Application) : AndroidViewModel(app) {


    private val alarmSetting: MutableLiveData<MutableList<AlarmSettings>> = MutableLiveData(
        mutableListOf()
    )

    var liveSettings: LiveData<MutableList<AlarmSettings>> = alarmSetting

    var alarmsUtility: AlarmsUtility = AlarmsUtility(app)

    val liveListOfSettings: LiveData<MutableList<AlarmSettings>> = liveData {
        val data = alarmsUtility.getAlarmsFromJson()
        var id = 0
        for (statement in data)
            statement.id = id++
        emit(data.toMutableList())
    }

    fun removeAlarm(alarm: AlarmSettings) {
        alarmSetting.value?.remove(alarm)
    }

    fun addAlarm(alarm: AlarmSettings) {
        if (alarmSetting.value == null)
            alarmSetting.value = mutableListOf()
        alarmSetting.value?.add(alarm)
    }

}