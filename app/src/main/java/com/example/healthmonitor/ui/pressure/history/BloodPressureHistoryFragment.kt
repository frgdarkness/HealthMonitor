package com.example.healthmonitor.ui.pressure.history

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import com.example.healthmonitor.R
import com.example.healthmonitor.data.local.AppDatabase
import com.example.healthmonitor.data.repository.PressureRepositoryImpl
import com.example.healthmonitor.databinding.FragmentBloodPressureHistoryBinding
import com.example.healthmonitor.ui.Helper
import com.google.android.material.datepicker.MaterialDatePicker

class BloodPressureHistoryFragment : Fragment() {

    private lateinit var viewModel: BloodPressureHistoryViewModel
    private lateinit var binding: FragmentBloodPressureHistoryBinding
    private lateinit var adapter: PressureAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBloodPressureHistoryBinding.inflate(inflater, container, false)
        val pressureQueries = AppDatabase.getInstance(requireContext()).pressurestoreQueries
        val repo = PressureRepositoryImpl(pressureQueries)
        viewModel = ViewModelProvider(this, PressureHistoryViewModelFactory(repo))[BloodPressureHistoryViewModel::class.java]
        initView()
        initEvent()
        observeViewModel()
        viewModel.getPressureList()
        return binding.root
    }

    private fun initView() {
        adapter = PressureAdapter(requireContext()) {
            Log.d("asd", "select pressure ($it)")
        }
        binding.recyclerViewHistoryPressure.adapter = adapter
    }

    private fun initEvent() {
        binding.cardViewCalendar.setOnClickListener {
            createCalendarPicker()
        }
    }

    private fun createCalendarPicker() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setSelection(
                Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
            )
            .build()
        dateRangePicker.addOnPositiveButtonClickListener {
            viewModel.filterData(it.first, it.second)
            val fromDate = Helper.convertMillisToddMMyyyy(it.first)
            val toDate = Helper.convertMillisToddMMyyyy(it.second)
            binding.tvCalendar.text = getString(R.string.date_range, fromDate, toDate)
        }
        dateRangePicker.show(parentFragmentManager, "Material Date Range Picker")
    }

    private fun observeViewModel() {
        viewModel.pressureList.observe(viewLifecycleOwner, Observer {
            Log.d("asd", "pressureList = ${it.map { pressure ->  pressure.id to pressure.sys }}")
            adapter.loadData(it)
        })
        viewModel.filteredData.observe(viewLifecycleOwner, Observer {
            adapter.loadData(it)
        })
    }
}