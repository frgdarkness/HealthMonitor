package com.example.healthmonitor.ui.pressure.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthmonitor.data.repository.PressureRepository

class PressureHistoryViewModelFactory(private val repo: PressureRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(PressureRepository::class.java).newInstance(repo)
    }
}