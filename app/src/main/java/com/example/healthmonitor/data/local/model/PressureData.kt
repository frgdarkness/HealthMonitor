package com.example.healthmonitor.data.local.model

import com.example.healthmonitor.PRESSURE

data class PressureData(
    val id: Long,
    val dateTime: Long,
    val sys: Int,
    val dia: Int,
    val pulse: Int,
    val age: Int,
    val sex: Int
)

fun PressureData.toPressure() = PRESSURE(id, dateTime, sys.toLong(), dia.toLong(), pulse.toLong(), age, sex)

fun PRESSURE.toPressureData() = PressureData(id, dateTime, sys.toInt(), dia.toInt(), pulse.toInt(), age, sex)