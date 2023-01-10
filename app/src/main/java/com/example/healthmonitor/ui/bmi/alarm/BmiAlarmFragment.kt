package com.example.healthmonitor.ui.bmi.alarm

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthmonitor.R
import com.example.healthmonitor.databinding.FragmentBmiAlarmBinding
import com.example.healthmonitor.databinding.FragmentBmiBinding

class BmiAlarmFragment : Fragment() {

    private lateinit var viewModel: BmiAlarmViewModel
    private lateinit var binding: FragmentBmiAlarmBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBmiAlarmBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[BmiAlarmViewModel::class.java]
        return binding.root
    }
}