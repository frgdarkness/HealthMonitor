package com.example.healthmonitor.ui.pressure.calculator

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.navigation.fragment.findNavController
import com.example.healthmonitor.PressurestoreQueries
import com.example.healthmonitor.R
import com.example.healthmonitor.data.local.AppDatabase
import com.example.healthmonitor.data.local.model.PressureData
import com.example.healthmonitor.data.local.model.toPressure
import com.example.healthmonitor.data.repository.PressureRepository
import com.example.healthmonitor.data.repository.PressureRepositoryImpl
import com.example.healthmonitor.databinding.FragmentBloodPresureCalculatorBinding
import com.example.healthmonitor.ui.Helper
import com.example.healthmonitor.ui.pressure.PressureRangeEnum
import com.example.healthmonitor.ui.pressure.getPressureRange
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BloodPressureCalculatorFragment : Fragment() {

    private lateinit var viewModel: BloodPressureCalculatorViewModel
    private lateinit var binding: FragmentBloodPresureCalculatorBinding
    private lateinit var repository: PressureRepository

    companion object {
        const val DEFAULT_SYS = 90
        const val DEFAULT_DIA = 70
        const val DEFAULT_PULSE = 70
    }

    private var sys = DEFAULT_SYS
    private var dia = DEFAULT_DIA
    private var pulse = DEFAULT_PULSE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBloodPresureCalculatorBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[BloodPressureCalculatorViewModel::class.java]
        val pressureQueries = AppDatabase.getInstance(requireContext()).pressurestoreQueries
        repository = PressureRepositoryImpl(pressureQueries)
        initView()
        initEvent()
        return binding.root
    }

    private val onValueChangedListener =
        NumberPicker.OnValueChangeListener { numberPicker, oldValue, newValue ->
            when (numberPicker.id) {
                binding.numberPickerDia.id -> {
                    dia = newValue
                    analysisPressureValue()
                }
                binding.numberPickerSys.id -> {
                    sys = newValue
                    analysisPressureValue()
                }
                binding.numberPickerPulse.id -> {
                    analysisPressureValue()
                    pulse = newValue
                }
            }
        }

    private fun analysisPressureValue() {
        val pressureRange = getPressureRange(sys, dia)
        binding.apply {
            tvPressureRange.text = pressureRange.range
            tvPressureStatus.text = pressureRange.displayName
            tvPressureStatus.setTextColor(Color.parseColor(pressureRange.color))
            iconStatus.setColorFilter(Color.parseColor(pressureRange.color))
            tvAnalysis.text = getString(pressureRange.detailStringId)
        }
    }

    private fun initView() {
//        val data = PressureRangeEnum.values().toList()
//        val adapter = PressureRangeAdapter(data)
        binding.apply {
//            recyclerViewPressureRange.adapter = adapter
            numberPickerSys.maxValue = 200
            numberPickerSys.minValue = 20
            numberPickerSys.value = 90
            numberPickerSys.setOnValueChangedListener(onValueChangedListener)
            numberPickerDia.maxValue = 200
            numberPickerDia.minValue = 40
            numberPickerDia.value = 70
            numberPickerDia.setOnValueChangedListener(onValueChangedListener)
            numberPickerPulse.maxValue = 200
            numberPickerPulse.minValue = 40
            numberPickerPulse.value = 70

            cardViewDate.setOnClickListener {
                createDatePicker()
            }
            cardViewTime.setOnClickListener {
                createTimePicker()
            }
        }
        createChipGroup()
        loadDateTime()
    }

    private fun initEvent() {
        binding.apply {
            layoutRange.setOnClickListener {
                findNavController().navigate(R.id.action_bloodPressureCalculatorFragment_to_pressureRangeFragment)
            }
            btnSavePressure.setOnClickListener {
                val pressureData = PressureData(
                    0,
                    Helper.getCurrentMillis(),
                    numberPickerSys.value,
                    numberPickerDia.value,
                    numberPickerPulse.value,
                    0,
                    1
                )
                CoroutineScope(Dispatchers.IO).launch {
                    repository.addPressure(pressureData.toPressure())
                }
                findNavController().navigate(
                    BloodPressureCalculatorFragmentDirections.actionBloodPressureCalculatorFragmentToBloodPressureHistoryFragment()
                )
            }
        }
    }

    private fun createChipGroup() {
        val data = listOf("Day", "Afternoon", "Night", "After Breakfast", "Good", "Bad", "Tired")
        data.forEach {
            val chip = Chip(requireContext())
            chip.apply {
                text = it
                isCheckable = true
            }
            binding.chipGroup.addView(chip)
        }
    }

    private fun loadDateTime() {
        val millis = Helper.getCurrentMillis()
        val date = Helper.convertMillisToFullMonthName(millis)
        binding.tvDate.text = date
        val time = Helper.convertMillisToHHmm(millis)
        binding.tvTime.text = time
    }

    private fun createDatePicker() {
        val dateRangePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()
        dateRangePicker.addOnPositiveButtonClickListener {

            val date = Helper.convertMillisToFullMonthName(it)
            binding.tvDate.text = date
        }
        dateRangePicker.show(parentFragmentManager, "DATE_PICKER")
    }

    private fun createTimePicker() {
        val calendar = Calendar.getInstance()
        val clockFormat = TimeFormat.CLOCK_24H
        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setTitleText("Select time")
            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(Calendar.MINUTE))
            .build()

        timePicker.addOnPositiveButtonClickListener {
            val hour = timePicker.hour
            val minute = timePicker.minute
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            val millis = calendar.timeInMillis
            val time = calendar.time

            binding.tvTime.text = Helper.convertMillisToHHmm(millis)
        }
        timePicker.show(parentFragmentManager, "TIME_PICKER")
    }
}
