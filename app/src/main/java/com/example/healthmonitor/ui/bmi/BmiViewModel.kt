package com.example.healthmonitor.ui.bmi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BmiViewModel : ViewModel() {

    private val _heightValue = MutableLiveData<Float>()
    val heightValue: LiveData<Float>
        get() = _heightValue

    private val _heightUnitPosition = MutableLiveData<Int>()
    val heightUnitPosition: LiveData<Int>
        get() = _heightUnitPosition

    private val _weightValue = MutableLiveData<Float>()
    val weightValue: LiveData<Float>
        get() = _weightValue

    private val _weightUnitPosition = MutableLiveData<Int>()
    val weightUnitPosition: LiveData<Int>
        get() = _weightUnitPosition

    fun saveState(heightValue: Float, heightUnitPosition: Int, weightValue: Float, weightUnitPosition: Int) {
        this._heightValue.value = heightValue
        this._heightUnitPosition.value = heightUnitPosition
        this._weightValue.value = weightValue
        this._weightUnitPosition.value = weightUnitPosition
    }
}