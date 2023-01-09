package com.example.healthmonitor.data.repository

import com.example.healthmonitor.PRESSURE

interface PressureRepository {

    suspend fun addPressure(data: PRESSURE)

    suspend fun updatePressure(data: PRESSURE)

    suspend fun getAllPressure(): List<PRESSURE>

    suspend fun deletePressure(id: Long)

    suspend fun getLastPressure(): PRESSURE
}