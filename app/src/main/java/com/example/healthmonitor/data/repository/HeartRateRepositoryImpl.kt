package com.example.healthmonitor.data.repository

import com.example.healthmonitor.BMI
import com.example.healthmonitor.HeartRate
import com.example.healthmonitor.HeartratestoreQueries


class HeartRateRepositoryImpl(private val heartRateStoreQueries: HeartratestoreQueries): HeartRateRepository {
    override suspend fun addHeartRate(data: HeartRate) {
        heartRateStoreQueries.addOrUpdate(
            id = null,
            dateTime = data.dateTime,
            pulse = data.pulse,
            age = data.age,
            sex = data.sex
        )
    }

    override suspend fun updateHeartRate(data: HeartRate) {
        heartRateStoreQueries.addOrUpdate(
            id = data.id,
            dateTime = data.dateTime,
            pulse = data.pulse,
            age = data.age,
            sex = data.sex
        )
    }

    override suspend fun getAllHeartRate(): List<HeartRate> {
        return heartRateStoreQueries.getAllHeartRate().executeAsList()
    }

    override suspend fun deleteHeartRate(id: Long) {
        heartRateStoreQueries.deleteHeartRateById(id)
    }

    override suspend fun getLastHeartRate(): HeartRate {
        return heartRateStoreQueries.getLastHeartRate().executeAsOne()
    }

}