package com.example.alarmclock.ui.viwholders.alarmview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmclock.databinding.AlarmMenuExtendedBinding
import com.example.alarmclock.ui.viwholders.Holder

class AlarmViewMenuExtended(private val alarmAdapter: AlarmAdapter) :
    Holder<AlarmViewMenuExtended.ExtendedView>() {

    override fun createView(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding =
            AlarmMenuExtendedBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )

        return ExtendedView(binding)
    }

    override fun getItemCount(): Int = 6

    fun subtractMenu(position: Int) {
        alarmAdapter. sendUpdate(position)
    }

    override fun onBindViewHolder(holder: ExtendedView, position: Int) {
        with(holder.binding) {
            toggleExtendedView.setOnClickListener {
                subtractMenu(position)
            }
        }
    }

    inner class ExtendedView(val binding: AlarmMenuExtendedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            onBindViewHolder(this, position)
        }
    }
}