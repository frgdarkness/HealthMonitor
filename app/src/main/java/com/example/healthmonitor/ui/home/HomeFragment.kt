package com.example.healthmonitor.ui.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.healthmonitor.R
import com.example.healthmonitor.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    companion object {

    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        initEvent()
        return binding.root
    }

    private fun initEvent() {
        binding.apply {
            btnBMI.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_bmiFragment)
            }
            btnPressure.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_pressureFragment)
            }
            btnHeartRate.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_heartRateFragment)
            }
        }
    }
}