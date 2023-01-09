package com.example.healthmonitor.data.local.model

import com.example.healthmonitor.HeartRate

data class HeartRateData(
    val id: Long,
    val dateTime: Long,
    val pulse: Int,
    val age: Int,
    val sex: Int
)

fun HeartRateData.toHeartRate() = HeartRate(id, dateTime, pulse, age, sex)

fun HeartRate.toHeartRateData() = HeartRateData(id, dateTime, pulse, age, sex)