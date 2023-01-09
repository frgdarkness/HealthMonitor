package com.example.healthmonitor.ui.bmi

object BmiHelper {
    const val EMPTY_STRING = ""

    fun calculateBmiValue(cm: Int, kg: Int): Float {
        val met = cm.toFloat() / 100
        return kg.toFloat() / (met * met)
    }

    fun calculateHealthWeight(cm: Int): Pair<Int, Int> {
        val met = cm.toFloat() / 100
        val min = 18.5 * met * met
        val max = 24.9 * met * met
        return Pair(min.toInt(), max.toInt())
    }
}