package com.example.healthmonitor.ui.bmi.analysis

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.healthmonitor.BMI
import com.example.healthmonitor.R
import com.example.healthmonitor.data.local.AppDatabase
import com.example.healthmonitor.data.repository.BmiRepository
import com.example.healthmonitor.data.repository.BmiRepositoryImpl
import com.example.healthmonitor.databinding.FragmentBmiAnalysisBinding
import com.example.healthmonitor.ui.Helper
import com.example.healthmonitor.ui.bmi.BmiHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BmiAnalysisFragment : Fragment() {

    private lateinit var viewModel: BmiAnalysisViewModel
    private lateinit var binding: FragmentBmiAnalysisBinding
    private lateinit var bmiRepository: BmiRepositoryImpl

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBmiAnalysisBinding.inflate(inflater, container, false)
        val bmiQueries = AppDatabase.getInstance(requireContext()).bmistoreQueries
        bmiRepository = BmiRepositoryImpl(bmiQueries)
        val viewModelFactory = BmiAnalysisViewModelFactory(bmiRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[BmiAnalysisViewModel::class.java]
        initView()
        observerViewModel()
        viewModel.getBmiData()
        return binding.root
    }

    private fun initView() {
        val adapter = BmiRangeAdapter(BmiRangeEnum.values().toList())
        binding.recyclerViewBmiRange.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            val bmiValues = bmiRepository.getAllBmi()
            Log.d("asd", "bmiValues = ${bmiValues.map { "id = ${it.id} - BMI = ${it.bmiValue}" }}")
        }

        binding.apply {
            btnCalculateAgain.setOnClickListener {
                findNavController().popBackStack()
            }
            btnAdd.setOnClickListener {
                viewModel.saveBmiData()
                findNavController().navigate(
                    BmiAnalysisFragmentDirections.actionBmiAnalysisFragmentToBmiHistoryFragment()
                )
            }
        }
    }

    private fun observerViewModel() {
        viewModel.bmiData.observe(viewLifecycleOwner, Observer { bmi ->
            Log.d("asd", "observe bmi = $bmi")
            binding.apply {
                val bmiRange = bmi.bmiValue.getBmiRange()
                tvBmiAnalysis.setTextColor(Color.parseColor(bmiRange.color))
                tvBmiAnalysis.text = bmiRange.displayName
                tvBmiValue.text = String.format("%.2f", bmi.bmiValue)
                val (minWeight, maxWeight) = BmiHelper.calculateHealthWeight(bmi.height)
                tvHealthyWeightValue.text =
                    getString(R.string.healthy_weight_range, minWeight, maxWeight)
            }
        })
    }
}