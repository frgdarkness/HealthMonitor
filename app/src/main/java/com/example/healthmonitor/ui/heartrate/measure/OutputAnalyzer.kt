package com.example.healthmonitor.ui.heartrate.measure

import android.util.Log
import kotlin.math.ceil
import kotlin.math.max
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class OutputAnalyzer(
    private val measureCalculator: MeasureCalculator
) {

    companion object {
        private const val MEASURE_INTERVAL = 45L
        private const val MEASURE_LENGTH = 15000L
        private const val CLIP_LENGTH = 3500
        private const val VALLEY_DETECT_SIZE = 13
    }

    private var detectedValleys = 0
    private var ticksPassed = 0
    private var store: MeasureStore? = null
    private val valleys = mutableListOf<Long>()

    private fun detectValley(store: MeasureStore): Boolean {
        val subList = store.getLastStdValues(VALLEY_DETECT_SIZE) ?: return false
        return if (subList.size < VALLEY_DETECT_SIZE) {
            false
        } else {
            val index = ceil(VALLEY_DETECT_SIZE / 2f).toInt()
            val referenceValue = subList[index].measurement
            for (measurement in subList) {
                if (measurement.measurement < referenceValue) return false
            }

            // filter out consecutive measurements due to too high measurement rate
            return (subList[index].measurement != subList[index - 1].measurement)
        }

    }

    /**
     * return pair of pulse value and progress
     */
    suspend fun measure() = flow<Pair<Int, Int>> {
        val store = MeasureStore()
        val maxInterval = (MEASURE_LENGTH / MEASURE_INTERVAL).toInt()
        var detectedValleys = 0
        val valleys = mutableListOf<Long>()

        for (intervalTime in 0..maxInterval) {
            val timeUntilFinished = MEASURE_LENGTH - MEASURE_INTERVAL * intervalTime
            emit(-1 to intervalTime*100/maxInterval)
            if (intervalTime*MEASURE_INTERVAL < CLIP_LENGTH) {
                delay(MEASURE_INTERVAL)
                emit(Pair(intervalTime / maxInterval, -1))
                continue
            }
            store.add(measureCalculator.calculateMeasure())
            if (detectValley(store)) {
                Log.d("asd", "detectValley = true")
                detectedValleys++
                valleys.add(store.getLastTimestamp().time)
                val currentTime = (MEASURE_LENGTH - timeUntilFinished - CLIP_LENGTH) / 1000f
                val pulse = if (valleys.size == 1) {
                    60f * detectedValleys / max(1f, currentTime)
                } else {
                    60f * (detectedValleys - 1) / max(
                        1f,
                        (valleys.last() - valleys.first()) / 1000f
                    )
                }
                val cycle = detectedValleys
                emit(pulse.toInt() to -1)
            }
            if (intervalTime == maxInterval) {
                Log.d("asd", "last interval")
                val stdValues = store.getStdValues()
                if (valleys.size == 0) {
                    Log.d("asd", "no valleys detected")
                    return@flow
                }
                val pulse =
                    60 * (detectedValleys - 1) / max(1f, (valleys.last() - valleys.first()) / 1000f)
                emit(pulse.toInt() to 100)

            }
            delay(MEASURE_INTERVAL)
        }
    }

    suspend fun measurePulse() = flow<Pair<Int, Int>> {
        val store = MeasureStore()
        detectedValleys = 0
        ticksPassed = 0
        valleys.clear()
        var pulse = 0f
        var cycle = 0
        var totalTime = 0f
        var millisUntilFinished = MEASURE_LENGTH
        var intervalTime = 0
        val maxInterval = MEASURE_LENGTH/ MEASURE_INTERVAL
        while (millisUntilFinished >= 0) {
            intervalTime++
            val progress = intervalTime*100/maxInterval.toInt()
            Log.d("asd", "progress by interval = $progress")
            emit(Pair(progress, -1))
            if (intervalTime * MEASURE_INTERVAL < CLIP_LENGTH) {
                delay(MEASURE_INTERVAL)
                continue
            }
            val measurement = measureCalculator.calculateMeasure()
            millisUntilFinished = MEASURE_LENGTH - intervalTime * MEASURE_INTERVAL
            store?.add(measurement)
            if (detectValley(store)) {
                detectedValleys++
                valleys.add(store!!.getLastTimestamp().time)
                // in 13 seconds (13000 milliseconds), I expect 15 valleys. that would be a pulse of 15 / 130000 * 60 * 1000 = 69

                pulse = if (valleys.size == 1) {
                    60f * detectedValleys / 1f.coerceAtLeast((MEASURE_LENGTH - millisUntilFinished - CLIP_LENGTH) / 1000f)
                } else {
                    60f * (detectedValleys - 1) / 1f.coerceAtLeast(
                        (valleys[valleys.size - 1] - valleys[0]) / 1000f
                    )
                }
                cycle = detectedValleys
                totalTime = 1f * (MEASURE_LENGTH - millisUntilFinished - CLIP_LENGTH) / 1000f

                emit(Pair(-1, pulse.toInt()))
                Log.d("asd", "pulse = $pulse, cycle = $cycle, totalTime = $totalTime")
            } else {
//                emit(Pair(progress, -1))
            }
            if (millisUntilFinished - MEASURE_INTERVAL <= 0) {
                Log.d("asd", "last time")
                val stdValues = store?.getStdValues()
                if (stdValues.isNullOrEmpty()) {
                    Log.d("asd", "No valleys detected")
                }
                pulse =
                    60f * (detectedValleys - 1) / 1f.coerceAtLeast((valleys[valleys.size - 1] - valleys[0]) / 1000f)
                cycle = detectedValleys - 1
                totalTime = 1f * ((valleys.last() - valleys.first()) / 1000f)
                val result = "pulse = $pulse, cycle = $cycle, totalTime = $totalTime"
                Log.d("asd", "last interval -> result: $result")
                emit(Pair(100, pulse.toInt()))
            }
            delay(MEASURE_INTERVAL)
        }
    }

    suspend fun testFlow() = flow<String> {
        store = MeasureStore()
        detectedValleys = 0
        ticksPassed = 0
        valleys.clear()
        var pulse = 0f
        var cycle = 0
        var totalTime = 0f
        var millisUntilFinished = MEASURE_LENGTH
        var intervalTime = 0
        while (millisUntilFinished >= 0) {
            intervalTime++
            if (intervalTime * MEASURE_INTERVAL < CLIP_LENGTH) {
                delay(MEASURE_INTERVAL)
                continue
            }
            millisUntilFinished = MEASURE_LENGTH - intervalTime * MEASURE_INTERVAL
            emit("interval: $intervalTime - millisUntilFinished: $millisUntilFinished")
            if (millisUntilFinished - MEASURE_INTERVAL <= 0) {
                emit("last interval ($intervalTime) - millisUntilFinished: $millisUntilFinished")
            }
            delay(MEASURE_INTERVAL)
        }
    }

    interface MeasureCalculator {
        suspend fun calculateMeasure(): Int
    }
}