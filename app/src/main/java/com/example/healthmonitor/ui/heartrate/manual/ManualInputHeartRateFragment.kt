package com.example.healthmonitor.ui.heartrate.manual

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker.OnValueChangeListener
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.healthmonitor.R
import com.example.healthmonitor.data.local.AppDatabase
import com.example.healthmonitor.data.local.model.HeartRateData
import com.example.healthmonitor.data.local.model.toHeartRate
import com.example.healthmonitor.data.repository.HeartRateRepository
import com.example.healthmonitor.data.repository.HeartRateRepositoryImpl
import com.example.healthmonitor.databinding.FragmentManualInputHeartRateBinding
import com.example.healthmonitor.ui.Helper
import com.example.healthmonitor.ui.heartrate.getPulseRange
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManualInputHeartRateFragment : Fragment() {


    private lateinit var viewModel: ManualInputHeartRateViewModel
    private lateinit var binding: FragmentManualInputHeartRateBinding
    private lateinit var repo: HeartRateRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentManualInputHeartRateBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[ManualInputHeartRateViewModel::class.java]
        initView()
        return binding.root
    }

    private val onNumberChange = OnValueChangeListener { view, oldValue, newValue ->
        TODO("Not yet implemented")
    }

    private fun initView() {
        createChipGroup()
        val args = ManualInputHeartRateFragmentArgs.fromBundle(requireArguments())
        Log.d("asd", "pulse from bundle = ${args.pulse}")
        binding.apply {
            numberPickerAge.minValue = 5
            numberPickerAge.maxValue = 150
            numberPickerAge.value = 20

            numberPickerPulse.minValue = 20
            numberPickerPulse.maxValue = 200
            numberPickerPulse.value = args.pulse

            numberPickerGender.minValue = 0
            numberPickerGender.maxValue = 2
            numberPickerGender.displayedValues = arrayOf("Male", "Female", "Other")

            val queries = AppDatabase.getInstance(requireContext()).heartratestoreQueries
            repo = HeartRateRepositoryImpl(queries)

            initEvent()
            numberPickerPulse.setOnValueChangedListener { numberPicker, oldValue, newValue ->
                val pulseRange = newValue.getPulseRange()
                tvHeartRateStatus.text = pulseRange.displayName
                tvHeartRateStatus.setTextColor(Color.parseColor(pulseRange.color))
                iconStatus.setColorFilter(Color.parseColor(pulseRange.color))
                tvHeartRateRange.text = pulseRange.status
                tvAnalysis.text = getString(pulseRange.detailStringId)
            }

            cardViewDate.setOnClickListener {
                createDatePicker()
            }
            cardViewTime.setOnClickListener {
                createTimePicker()
            }
            layoutEditTag.setOnClickListener {
                Toast.makeText(requireContext(), "edit tag", Toast.LENGTH_SHORT).show()
            }
            loadDateTime()
        }
    }

    private fun initEvent() {
        binding.apply {

            val queries = AppDatabase.getInstance(requireContext()).heartratestoreQueries
            repo = HeartRateRepositoryImpl(queries)

            numberPickerPulse.setOnValueChangedListener { numberPicker, oldValue, newValue ->
                val pulseRange = newValue.getPulseRange()
                tvHeartRateStatus.text = pulseRange.displayName
                tvHeartRateStatus.setTextColor(Color.parseColor(pulseRange.color))
                iconStatus.setColorFilter(Color.parseColor(pulseRange.color))
                tvHeartRateRange.text = pulseRange.status
                tvAnalysis.text = getString(pulseRange.detailStringId)
            }

            cardViewDate.setOnClickListener {
                createDatePicker()
            }
            cardViewTime.setOnClickListener {
                createTimePicker()
            }
            layoutEditTag.setOnClickListener {
                Toast.makeText(requireContext(), "edit tag", Toast.LENGTH_SHORT).show()
            }
            btnSaveHeartRate.setOnClickListener {

                    val heartRateData = HeartRateData(
                        id = 0,
                        dateTime = Helper.getCurrentMillis(),
                        pulse = numberPickerPulse.value,
                        age = numberPickerAge.value,
                        sex = 1
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        repo.addHeartRate(heartRateData.toHeartRate())
                    }
                    findNavController().navigate(
                        R.id.action_manualInputHeartRateFragment_to_heartRateHistoryFragment
                    )

            }
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

    private fun createChipGroup() {
        val data = listOf("Day", "Afternoon", "Night", "After Breakfast", "Good", "Bad", "Tired")
        data.forEach {
            val chip = Chip(requireContext())
//            val drawable = ChipDrawable.createFromAttributes(requireContext(), null, 0, R.style.CustomChipStyle)
            chip.apply {
                text = it
                isCheckable = true
//                setChipDrawable(drawable)
            }
            binding.chipGroup.addView(chip)
        }
    }
}