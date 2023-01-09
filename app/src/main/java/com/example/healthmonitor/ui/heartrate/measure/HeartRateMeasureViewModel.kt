package com.example.healthmonitor.ui.heartrate.measure

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class HeartRateMeasureViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    private val _isMeasuring = MutableLiveData<Boolean>()
    val isMeasuring: LiveData<Boolean> get() = _isMeasuring

    suspend fun test(getValue: () -> Int) {
        repeat(10) {
            val data = getValue()
            Log.d("asd", "value = $data")
            delay(100)
        }
    }

    fun setMeasureState(isMeasuring: Boolean) {
        _isMeasuring.value = isMeasuring
    }
}