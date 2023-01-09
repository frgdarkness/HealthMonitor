package com.example.healthmonitor.ui.heartrate.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmonitor.data.local.model.HeartRateData
import com.example.healthmonitor.data.local.model.toHeartRateData
import com.example.healthmonitor.data.repository.HeartRateRepository
import kotlinx.coroutines.launch

class HeartRateHistoryViewModel(private val repo: HeartRateRepository) : ViewModel() {

    private val _heartRateList = MutableLiveData<List<HeartRateData>>()
    val heartRateList: LiveData<List<HeartRateData>>
        get() = _heartRateList

    private val _filteredData = MutableLiveData<List<HeartRateData>>()
    val filteredData: LiveData<List<HeartRateData>>
        get() = _filteredData

    fun getHeartRateList() {
        viewModelScope.launch {
            val data = repo.getAllHeartRate()
            _heartRateList.value = data.map { it.toHeartRateData() }
        }
    }

    fun filterDate(from: Long, to: Long) {
        heartRateList.value?.filter {
            it.dateTime in from..to
        } ?.let { data ->
            _filteredData.value = data
        }
    }
}