package com.example.healthmonitor.ui.pressure.trends

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthmonitor.R
import com.example.healthmonitor.databinding.FragmentPressureTrendsBinding

class PressureTrendsFragment : Fragment() {

    companion object {
        fun newInstance() = PressureTrendsFragment()
    }

    private lateinit var viewModel: PressureTrendsViewModel
    private lateinit var binding: FragmentPressureTrendsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPressureTrendsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[PressureTrendsViewModel::class.java]
        return binding.root
    }
}