package com.example.healthmonitor.ui.bmi.calculator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmonitor.ui.bmi.MAX_AGE
import com.example.healthmonitor.ui.bmi.MAX_WEIGHT
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BmiCalculatorViewModel : ViewModel() {

    private var job: Job? = null
    private val _weight = MutableLiveData<Int>()
    val weight: LiveData<Int>
        get() = _weight

    private val _age = MutableLiveData<Int>()
    val age: LiveData<Int>
        get() = _age

    private val _height = MutableLiveData<Int>()
    val height: LiveData<Int>
        get() = _height

    private val _isMale = MutableLiveData<Boolean>()
    val isMale: LiveData<Boolean>
        get() = _isMale

    fun incWeight() {
        _weight.value = _weight.value?.plus(1)
    }

    fun decWeight() {
        _weight.value = _weight.value?.minus(1)
    }

    fun setWeight(value: Int) {
        _weight.value = value
    }

    fun incAge() {
        _age.value = _age.value?.plus(1)
    }

    fun decAge() {
        _age.value = _age.value?.minus(1)
    }

    fun setAge(value: Int) {
        _age.value = value
    }

    fun setHeight(value: Int) {
        _height.value = value
    }

    fun isMale(isMale: Boolean) {
        _isMale.value = isMale
    }

    fun changeWeight(isInc: Boolean) {
        Log.d("asd", "changeWeight")
        job?.cancel()
        job = viewModelScope.launch {
            while((_weight.value ?: 0) in 0..MAX_WEIGHT) {
                if (isInc) {
                    incWeight()
                } else decWeight()
                delay(100)
            }
        }
    }

    fun changeAge(isInc: Boolean) {
        Log.d("asd", "changeAge")
        job?.cancel()
        job = viewModelScope.launch {
            while((_age.value ?: 0) in 0..MAX_AGE) {
                if (isInc) {
                    incAge()
                } else decAge()
                delay(100)
            }
        }
    }

    fun cancelChangeValueData() {
        Log.d("asd", "cancel change value")
        job?.cancel()
    }
}