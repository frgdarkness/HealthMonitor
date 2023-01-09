package com.example.healthmonitor.ui.bmi.history

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
import com.example.healthmonitor.data.repository.BmiRepositoryImpl
import com.example.healthmonitor.databinding.FragmentBmiHistoryBinding
import com.example.healthmonitor.ui.Helper
import com.google.android.material.datepicker.MaterialDatePicker

class BmiHistoryFragment : Fragment() {

    private lateinit var viewModel: BmiHistoryViewModel
    private lateinit var binding: FragmentBmiHistoryBinding
    private lateinit var adapter: BmiRecordAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBmiHistoryBinding.inflate(inflater, container, false)
        val bmiQueries = AppDatabase.getInstance(requireContext()).bmistoreQueries
        val repo = BmiRepositoryImpl(bmiQueries)
        viewModel = ViewModelProvider(this, BmiHistoryViewModelFactory(repo))[BmiHistoryViewModel::class.java]
        initView()
        initEvent()
        observeViewModel()
        viewModel.getBmiList()
        return binding.root
    }

    private fun initView() {
        adapter = BmiRecordAdapter(requireContext()) {
            Log.d("asd", "click at [$it]")
        }
        binding.recyclerViewHistoryBmi.adapter = adapter
//        binding.calendarPicker.
    }

    private fun observeViewModel() {
        viewModel.bmiList.observe(viewLifecycleOwner, Observer {
            adapter.loadData(it)

        })
        viewModel.filteredData.observe(viewLifecycleOwner, Observer {
            adapter.loadData(it)
        })
    }

    private fun initEvent() {
        binding.apply {
            cardViewCalendar.setOnClickListener {
                createCalendarPicker()
            }
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

}