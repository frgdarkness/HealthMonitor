package com.example.healthmonitor.ui.bmi.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.healthmonitor.data.repository.BmiRepository

class BmiAnalysisViewModelFactory(private val bmiRepository: BmiRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(BmiRepository::class.java).newInstance(bmiRepository)
    }
}