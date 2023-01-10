package com.example.healthmonitor.ui.pressure.range

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.healthmonitor.R
import com.example.healthmonitor.databinding.FragmentPressureRangeBinding
import com.example.healthmonitor.ui.pressure.PressureRangeEnum
import com.example.healthmonitor.ui.pressure.calculator.PressureRangeAdapter

class PressureRangeFragment : Fragment() {

    private lateinit var viewModel: PressureRangeViewModel
    private lateinit var binding: FragmentPressureRangeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPressureRangeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[PressureRangeViewModel::class.java]
        initView()
        initEvent()
        return binding.root
    }

    private fun initEvent() {
        binding.btnGotIt.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initView() {
        val data = PressureRangeEnum.values().toList()
        val adapter = PressureRangeAdapter(requireContext(), data)
        binding.recyclerViewPressureRange.adapter = adapter
    }
}