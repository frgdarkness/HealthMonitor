package com.example.healthmonitor.ui.bmi.analysis

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmonitor.BMI
import com.example.healthmonitor.data.repository.BmiRepository
import kotlinx.coroutines.launch

class BmiAnalysisViewModel(private val bmiRepository: BmiRepository) : ViewModel() {

    private val _bmiData = MutableLiveData<BMI>()
    val bmiData: LiveData<BMI>
        get() = _bmiData

    fun getBmiData() {
        viewModelScope.launch {
            val bmi = bmiRepository.getLastBmi()
            _bmiData.value = bmi
            bmiRepository.deleteBmi(bmi.id)
        }
    }

    fun saveBmiData() {
        viewModelScope.launch {
            bmiData.value?.let {
                bmiRepository.addBmi(it)
            }
        }
    }
}