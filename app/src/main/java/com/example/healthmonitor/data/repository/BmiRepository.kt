package com.example.healthmonitor.data.repository

import com.example.healthmonitor.BMI

interface BmiRepository {

    suspend fun addBmi(data: BMI)

    suspend fun updateBmi(data: BMI)

    suspend fun getAllBmi(): List<BMI>

    suspend fun deleteBmi(id: Long)

    suspend fun getLastBmi(): BMI
}