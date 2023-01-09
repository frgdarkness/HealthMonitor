package com.example.healthmonitor.ui.heartrate

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.healthmonitor.databinding.FragmentHeartRateBinding

class HeartRateFragment : Fragment() {

    companion object {
        fun newInstance() = HeartRateFragment()
    }

    private lateinit var binding: FragmentHeartRateBinding
    private lateinit var viewModel: HeartRateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeartRateBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HeartRateViewModel::class.java]

        initEvent()
        return binding.root
    }

    private fun initEvent() {
        binding.apply {
            btnGoHeartRateHistory.setOnClickListener {
                findNavController().navigate(
                    HeartRateFragmentDirections.actionHeartRateFragmentToHeartRateHistoryFragment()
                )
            }
            btnGoMeasureHeartRate.setOnClickListener {
                findNavController().navigate(
                    HeartRateFragmentDirections.actionHeartRateFragmentToHeartRateMeasureFragment()
                )
            }
            btnGoHeartRateManual.setOnClickListener {
                findNavController().navigate(
                    HeartRateFragmentDirections.actionHeartRateFragmentToManualInputHeartRateFragment(80)
                )
            }
        }
    }

}