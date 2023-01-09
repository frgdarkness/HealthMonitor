package com.example.healthmonitor.ui.heartrate.history

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import com.example.healthmonitor.R
import com.example.healthmonitor.data.local.AppDatabase
import com.example.healthmonitor.data.repository.HeartRateRepositoryImpl
import com.example.healthmonitor.databinding.FragmentHeartRateHistoryBinding
import com.example.healthmonitor.ui.Helper
import com.google.android.material.datepicker.MaterialDatePicker

class HeartRateHistoryFragment : Fragment() {

    private lateinit var viewModel: HeartRateHistoryViewModel
    private lateinit var binding: FragmentHeartRateHistoryBinding
    private lateinit var adapter: HeartRateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeartRateHistoryBinding.inflate(inflater, container, false)
        val hearRateQueries = AppDatabase.getInstance(requireContext()).heartratestoreQueries
        val repo = HeartRateRepositoryImpl(hearRateQueries)
        viewModel = ViewModelProvider(this, HeartRateHistoryViewModelFactory(repo))[HeartRateHistoryViewModel::class.java]
        initView()
        initEvent()
        observeViewModel()
        viewModel.getHeartRateList()
        return binding.root
    }

    private fun initEvent() {
        binding.cardViewCalendar.setOnClickListener {
            createCalendarPicker()
        }
    }

    private fun initView() {
        adapter = HeartRateAdapter(requireContext()) {
            Toast.makeText(requireContext(), "onClick at #$it", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerViewHistoryBmi.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.heartRateList.observe(viewLifecycleOwner, Observer {
            Log.d("asd", "heartRateData = $it")
            adapter.loadData(it)
        })
        viewModel.filteredData.observe(viewLifecycleOwner, Observer {
            Log.d("asd", "filteredData = $it")
            adapter.loadData(it)
        })
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
            viewModel.filterDate(it.first, it.second)
            val fromDate = Helper.convertMillisToddMMyyyy(it.first)
            val toDate = Helper.convertMillisToddMMyyyy(it.second)
            binding.tvCalendar.text = getString(R.string.date_range, fromDate, toDate)
        }
        dateRangePicker.show(parentFragmentManager, "Material Date Range Picker")
    }
}