package com.example.healthmonitor.ui.bmi.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthmonitor.data.repository.BmiRepository

class BmiHistoryViewModelFactory(private val bmiRepository: BmiRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(BmiRepository::class.java).newInstance(bmiRepository)
    }
}