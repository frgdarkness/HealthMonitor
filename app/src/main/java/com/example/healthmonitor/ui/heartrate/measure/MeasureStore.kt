package com.example.healthmonitor.ui.heartrate.measure

import android.util.Log
import androidx.camera.core.Logger
import java.util.*

class MeasureStore {

    companion object {

        private const val ROLLING_AVERAGE_SIZE = 4
    }

    private var minimum = Int.MAX_VALUE
    private var maximum = Int.MIN_VALUE
    private val measurements = mutableListOf<Measurement<Int>>()

    fun add(measurementValue: Int) {
        val measurement = Measurement<Int>(Date(), measurementValue)
        measurements.add(measurement)
        minimum = measurementValue.coerceAtMost(minimum)
        maximum = measurementValue.coerceAtLeast(minimum)
    }

    fun getStdValues(): List<Measurement<Float>> {
        val stdValues = mutableListOf<Measurement<Float>>()
        for (i in 0 until measurements.size) {
            var sum = 0
            for (rollingAverageCounter in 0 until ROLLING_AVERAGE_SIZE) {
                sum += measurements[0.coerceAtLeast(i - rollingAverageCounter)].measurement
            }

            val stdValue = Measurement(
                measurements[i].timestamp,
                (sum.toFloat()/ ROLLING_AVERAGE_SIZE - minimum) / (maximum - minimum)
            )
            stdValues.add(stdValue)
        }
        resetData()
        return stdValues
    }

    private fun resetData() {
        minimum = Int.MAX_VALUE
        maximum = Int.MIN_VALUE
        measurements.clear()
    }

    fun getLastStdValues(count: Int): List<Measurement<Int>> {
        return if (count < measurements.size) {
            measurements.subList(measurements.size - 1 - count, measurements.size - 1)
        } else measurements
    }

    fun getLastTimestamp(): Date {
        return measurements.lastOrNull()?.timestamp ?: kotlin.run {
            Log.d("asd", "getLastTimestamp = null")
            Date()
        }
    }
}