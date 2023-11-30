package com.example.alarmclock.ui.viwholders.alarmview

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmSettings
import com.example.alarmclock.ui.home.HomeFragment
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmViewModel

class AlarmAdapter(
    val alarms: MutableList<AlarmSettings>,
    val viewModel: AlarmViewModel,
    val homeFragment: HomeFragment
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //private val  holder : Holder<AlarmViewMenu, AlarmViewMenu.SubtractedView>? = null
    private val alarmViewMenu: AlarmViewMenu = AlarmViewMenu(this)
    private var position = 0

    init {
        onSetAddClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return alarmViewMenu.createView(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AlarmViewMenu.SubtractedView -> holder.bind(position)
        }
    }

    fun save() {
        viewModel.alarmsUtility.saveAlarmsToJson(alarms)
    }

    fun sendUpdate(position: Int) {
        this.position = position
        notifyDataSetChanged()
        //notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
           return alarms.size
    }

    fun onSetAddClick() {
        homeFragment.binding.addAlarm.setOnClickListener {
            alarms.add(AlarmSettings())
            save()
            sendUpdate(position)
        }
    }

}

enum class Mode {
    NORMAL,
    EXTENDED
}