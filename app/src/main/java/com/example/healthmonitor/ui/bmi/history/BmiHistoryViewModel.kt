package com.example.healthmonitor.ui.bmi.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmonitor.BMI
import com.example.healthmonitor.data.repository.BmiRepository
import kotlinx.coroutines.launch

class BmiHistoryViewModel(private val bmiRepository: BmiRepository) : ViewModel() {

    private val _bmiList = MutableLiveData<List<BMI>>()
    val bmiList: LiveData<List<BMI>>
        get() = _bmiList

    private val _filteredData = MutableLiveData<List<BMI>>()
    val filteredData: LiveData<List<BMI>>
        get() = _filteredData

    fun getBmiList() {
        viewModelScope.launch {
            val data = bmiRepository.getAllBmi()
            _bmiList.value = data
        }
    }

    fun filterData(from: Long, to: Long) {
        bmiList.value?.filter {
            it.dateTime in from..to
        } ?.let { data ->
            _filteredData.value = data
        }
    }
}