package com.example.healthmonitor.ui.bmi

import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.healthmonitor.R
import com.example.healthmonitor.databinding.FragmentBmiBinding

class BmiFragment : Fragment() {

    companion object {

    }

    private lateinit var viewModel: BmiViewModel
    private lateinit var binding: FragmentBmiBinding
    private val heightUnitItems = HeightMeasureUnit.values().map { it.displayName }
    private val weightUnitItems = WeightMeasureUnit.values().map { it.displayName }
    private var adapterHeight: ArrayAdapter<String>? = null
    private var adapterWeight: ArrayAdapter<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentBmiBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[BmiViewModel::class.java]

        binding.apply {
            btnGoCalculator.setOnClickListener {
                findNavController().navigate(R.id.action_bmiFragment_to_bmiCalculatorFragment)
            }
            btnGoHistory.setOnClickListener {
                findNavController().navigate(R.id.action_bmiFragment_to_bmiHistoryFragment)
            }
            btnGoTrend.setOnClickListener {
                findNavController().navigate(R.id.action_bmiFragment_to_bmiTrendsFragment)
            }
            btnGoAlarm.setOnClickListener {
                findNavController().navigate(R.id.action_bmiFragment_to_bmiAlarmFragment)
            }
        }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        Log.d("asd", "onPause")
    }

    override fun onStop() {
        super.onStop()
//        viewModel.saveState(
//            binding.edtHeight.text.toString().toFloat(),
//            heightUnitPosition = binding.dropdownMenuHeight.select
//        )
        Log.d("asd", "onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.d("asd", "onStart")
    }



}