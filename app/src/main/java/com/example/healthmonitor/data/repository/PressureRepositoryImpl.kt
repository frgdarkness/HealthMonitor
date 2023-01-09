package com.example.healthmonitor.data.repository

import com.example.healthmonitor.PRESSURE
import com.example.healthmonitor.PressurestoreQueries

class PressureRepositoryImpl(private val pressureQueries: PressurestoreQueries) : PressureRepository {
    override suspend fun addPressure(data: PRESSURE) {
        pressureQueries.addOrUpdatePressure(
            id = null,
            dateTime = data.dateTime,
            sys = data.sys,
            dia = data.dia,
            pulse = data.pulse,
            age = data.age,
            sex = data.sex
        )
    }

    override suspend fun updatePressure(data: PRESSURE) {
        pressureQueries.addOrUpdatePressure(
            id = data.id,
            dateTime = data.dateTime,
            sys = data.sys,
            dia = data.dia,
            pulse = data.pulse,
            age = data.age,
            sex = data.sex
        )
    }

    override suspend fun getAllPressure(): List<PRESSURE> {
        return pressureQueries.getAllPressure().executeAsList()
    }

    override suspend fun deletePressure(id: Long) {
        pressureQueries.deletePressureById(id)
    }

    override suspend fun getLastPressure(): PRESSURE {
        return pressureQueries.getLastPressure().executeAsOne()
    }
}