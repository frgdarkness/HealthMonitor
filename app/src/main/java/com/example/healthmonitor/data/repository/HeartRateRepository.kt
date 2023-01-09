package com.example.healthmonitor.data.repository

import com.example.healthmonitor.HeartRate

interface HeartRateRepository {

    suspend fun addHeartRate(data: HeartRate)

    suspend fun updateHeartRate(data: HeartRate)

    suspend fun getAllHeartRate(): List<HeartRate>

    suspend fun deleteHeartRate(id: Long)

    suspend fun getLastHeartRate(): HeartRate
}