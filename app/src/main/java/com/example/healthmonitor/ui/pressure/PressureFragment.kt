package com.example.healthmonitor.ui.pressure

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.healthmonitor.R
import com.example.healthmonitor.databinding.FragmentPressureBinding

class PressureFragment : Fragment() {

    companion object {
        fun newInstance() = PressureFragment()
    }

    private lateinit var viewModel: PressureViewModel
    private lateinit var binding: FragmentPressureBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPressureBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[PressureViewModel::class.java]

        binding.apply {
            btnGoBloodPressureHistory.setOnClickListener {
                findNavController().navigate(PressureFragmentDirections.actionPressureFragmentToBloodPressureHistoryFragment())
            }
            btnGoMeasureBloodPressure.setOnClickListener {
                findNavController().navigate(PressureFragmentDirections.actionPressureFragmentToBloodPressureCalculatorFragment())
            }
        }

        return binding.root
    }
}