package com.example.alarmclock.ui.viwholders

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmclock.ui.viwholders.alarmview.Mode


abstract class Holder<T> {

    abstract fun createView(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun onBindViewHolder(holder: T, position: Int)

    abstract  fun getItemCount(): Int
}