package com.example.healthmonitor.ui.pressure.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmonitor.PRESSURE
import com.example.healthmonitor.data.repository.PressureRepository
import kotlinx.coroutines.launch

class BloodPressureHistoryViewModel(private val repository: PressureRepository) : ViewModel() {

    private val _pressureList = MutableLiveData<List<PRESSURE>>()
    val pressureList: LiveData<List<PRESSURE>>
        get() = _pressureList

    private val _filteredData = MutableLiveData<List<PRESSURE>>()
    val filteredData: LiveData<List<PRESSURE>>
        get() = _filteredData

    fun getPressureList() {
        viewModelScope.launch {
            val data = repository.getAllPressure()
            _pressureList.value = data
        }
    }

    fun filterData(from: Long, to: Long) {
        pressureList.value?.filter {
            it.dateTime in from..to
        } ?.let { data ->
            _filteredData.value = data
        }
    }
}