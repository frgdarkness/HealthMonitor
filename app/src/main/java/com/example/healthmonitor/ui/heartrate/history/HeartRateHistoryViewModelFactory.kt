package com.example.healthmonitor.ui.heartrate.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthmonitor.data.repository.HeartRateRepository

class HeartRateHistoryViewModelFactory(private val repo: HeartRateRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HeartRateRepository::class.java).newInstance(repo)
    }
}
