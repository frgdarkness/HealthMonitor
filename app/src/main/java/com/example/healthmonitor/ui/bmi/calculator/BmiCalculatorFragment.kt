package com.example.healthmonitor.ui.bmi.calculator

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.healthmonitor.BMI
import com.example.healthmonitor.data.local.AppDatabase
import com.example.healthmonitor.data.repository.BmiRepository
import com.example.healthmonitor.data.repository.BmiRepositoryImpl
import com.example.healthmonitor.databinding.FragmentBmiCalculatorBinding
//import com.example.healthmonitor.toSexValue
import com.example.healthmonitor.ui.Helper
import com.example.healthmonitor.ui.bmi.BmiHelper
import com.example.healthmonitor.ui.bmi.DEFAULT_AGE
import com.example.healthmonitor.ui.bmi.DEFAULT_HEIGHT
import com.example.healthmonitor.ui.bmi.DEFAULT_WEIGHT
import com.example.healthmonitor.ui.bmi.MAX_HEIGHT
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BmiCalculatorFragment : Fragment() {

    private lateinit var viewModel: BmiCalculatorViewModel
    private lateinit var binding: FragmentBmiCalculatorBinding
    private var bmiRepository: BmiRepository? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBmiCalculatorBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[BmiCalculatorViewModel::class.java]

        initView()
        initEvent()
        observeViewModel()
        val bmiQueries = AppDatabase.getInstance(requireContext()).bmistoreQueries
        bmiRepository = BmiRepositoryImpl(bmiQueries)
        return binding.root
    }

    private fun observeViewModel() {
        binding.apply {
            viewModel.height.observe(viewLifecycleOwner, Observer {
                tvHeightValue.text = it.toString()
            })
            viewModel.age.observe(viewLifecycleOwner, Observer {
                tvAgeValue.text = it.toString()
            })
            viewModel.weight.observe(viewLifecycleOwner, Observer {
                tvWeightValue.text = it.toString()
            })
        }
    }

    private fun initView() {
        binding.apply {
            seekBarHeight.max = MAX_HEIGHT
            seekBarHeight.progress = DEFAULT_HEIGHT
        }
        viewModel.setHeight(DEFAULT_HEIGHT)
        viewModel.setWeight(DEFAULT_WEIGHT)
        viewModel.setAge(DEFAULT_AGE)
        loadDateTime()
        createChipGroup()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEvent() {
        binding.apply {
            btnIncWeight.setOnClickListener {
                viewModel.incWeight()
            }
            btnIncWeight.setOnTouchListener { _, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        viewModel.changeWeight(true)
                    }
                    MotionEvent.ACTION_UP -> {
                        viewModel.cancelChangeValueData()
                    }
                }
                false
            }
            btnDecWeight.setOnClickListener {
                viewModel.decWeight()
            }
            btnDecWeight.setOnTouchListener { _, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        viewModel.changeWeight(false)
                    }
                    MotionEvent.ACTION_UP -> {
                        viewModel.cancelChangeValueData()
                    }
                }
                true
            }
            btnIncAge.setOnClickListener {
                viewModel.incAge()
            }
            btnIncAge.setOnTouchListener { _, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        viewModel.changeAge(true)
                    }
                    MotionEvent.ACTION_UP -> {
                        viewModel.cancelChangeValueData()
                    }
                }
                true
            }
            btnDecAge.setOnClickListener {
                viewModel.decAge()
            }
            btnDecAge.setOnTouchListener { _, motionEvent ->
                when (motionEvent?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        viewModel.changeAge(false)
                    }
                    MotionEvent.ACTION_UP -> {
                        viewModel.cancelChangeValueData()
                    }
                }
                true
            }
            seekBarHeight.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    viewModel.setHeight(p1)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}

                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })
            btnCalculate.setOnClickListener {
                Log.d("asd", "btnCalculate")
                val height = tvHeightValue.text.toString().toIntOrNull() ?: DEFAULT_HEIGHT
                val weight = tvWeightValue.text.toString().toIntOrNull() ?: DEFAULT_WEIGHT
                val age = tvAgeValue.text.toString().toIntOrNull() ?: DEFAULT_AGE
                val bmiValue = BmiHelper.calculateBmiValue(height, weight)
                val (minWeight, maxWeight) = BmiHelper.calculateHealthWeight(height)
                Log.d("asd", "height: $height, weight: $weight")
                Log.d("asd", "bmi: $bmiValue, min: $minWeight, max: $maxWeight")
                addBmiDataToDB(weight, height, age, true)

                findNavController().navigate(
                    BmiCalculatorFragmentDirections.actionBmiCalculatorFragmentToBmiAnalysisFragment()
                )
            }
            cardViewDate.setOnClickListener {
                createDatePicker()
            }
            cardViewTime.setOnClickListener {
                createTimePicker()
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
            chip.apply {
                text = it
                isCheckable = true
            }
            binding.chipGroup.addView(chip)
        }
    }

    private fun addBmiDataToDB(weight: Int, height: Int, age: Int, isMale: Boolean) {
        val bmiValue = BmiHelper.calculateBmiValue(height, weight)
        CoroutineScope(Dispatchers.IO).launch {
            bmiRepository?.addBmi(
                BMI(
                    id = 0,
                    dateTime = Helper.getCurrentMillis(),
                    weight = weight,
                    height = height,
                    bmiValue = bmiValue,
                    age = age,
                    sex = 1
                )
            )
        }
    }
}