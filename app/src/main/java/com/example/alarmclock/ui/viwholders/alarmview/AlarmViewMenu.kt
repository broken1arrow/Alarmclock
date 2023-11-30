package com.example.alarmclock.ui.viwholders.alarmview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmSettings
import com.example.alarmclock.databinding.AlarmMenuBinding
import com.example.alarmclock.ui.home.HomeFragment
import com.example.alarmclock.ui.viwholders.Holder
import com.example.alarmclock.ui.viwholders.alarmview.utility.OnAlarmClick

class AlarmViewMenu(private val alarmAdapter: AlarmAdapter) :
    Holder<AlarmViewMenu.SubtractedView>() {

    private var parentView: ViewGroup? = null

    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        this.parentView = parent
        val binding = AlarmMenuBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SubtractedView(binding)
    }

    override fun onBindViewHolder(holder: SubtractedView, position: Int) {
        val instance = this
        with(holder.binding) {
            OnAlarmClick(this, instance, alarmAdapter.alarms[position], position)
        }
    }

    override fun getItemCount(): Int = 0

    fun homeFragment(): HomeFragment {
        return alarmAdapter.homeFragment
    }

    fun updateMenu(position: Int) {
        alarmAdapter.sendUpdate(position)
    }

    fun removeItem(position: Int) {
        alarmAdapter.viewModel.removeAlarm(AlarmSettings())
        alarmAdapter.save()
        alarmAdapter.sendUpdate(position)
    }

    fun addItem(position: Int) {
        alarmAdapter.viewModel.addAlarm(AlarmSettings())
        alarmAdapter.save()
        alarmAdapter.sendUpdate(position)
    }
    inner class SubtractedView(val binding: AlarmMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            onBindViewHolder(this, position)
        }
    }
}
