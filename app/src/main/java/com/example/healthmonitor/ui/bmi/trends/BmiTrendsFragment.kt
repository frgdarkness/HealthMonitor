package com.example.healthmonitor.ui.bmi.trends

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthmonitor.R
import com.example.healthmonitor.databinding.FragmentBmiTrendsBinding

class BmiTrendsFragment : Fragment() {

    private lateinit var viewModel: BmiTrendsViewModel
    private lateinit var binding: FragmentBmiTrendsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBmiTrendsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[BmiTrendsViewModel::class.java]
        return binding.root
    }
}