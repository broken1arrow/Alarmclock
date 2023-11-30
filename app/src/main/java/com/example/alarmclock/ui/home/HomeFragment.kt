package com.example.alarmclock.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmSettings
import com.example.alarmclock.databinding.FragmentHomeBinding
import com.example.alarmclock.ui.viwholders.alarmview.AlarmAdapter
import com.example.alarmclock.ui.viwholders.alarmview.cache.AlarmViewModel

class HomeFragment : Fragment() {

    var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    val binding get() = _binding!!
    private var viewModel: AlarmViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = mutableListOf<AlarmSettings>()
        list.add(AlarmSettings(1))
        list.add(AlarmSettings(1))
        list.add(AlarmSettings(1))
        list.add(AlarmSettings(1))

        viewModel = activity?.run {
            ViewModelProvider(this)[AlarmViewModel::class.java]
        }
        viewModel?.liveListOfSettings?.observe(viewLifecycleOwner) { statements ->
            val recyclerView = binding.recycle
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = AlarmAdapter(statements, viewModel!!, this)
        }
        /*
                val recyclerView = binding.recycle
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = AlarmAdapter(list,this )*/
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


