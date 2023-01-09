package com.example.healthmonitor.data.local.model

import com.example.healthmonitor.BMI

data class BmiData(
    val id: Long,
    val dateTime: Long,
    val weight: Int,
    val height: Int,
    val bmiValue: Float,
    val age: Int,
    val sex: Int
)

fun BmiData.toBmi() = BMI(id, dateTime, weight, height, bmiValue, age, sex)

fun BMI.toBmiData() = BmiData(id, dateTime, weight, height, bmiValue, age, sex)