package com.example.healthmonitor.data.repository

import com.example.healthmonitor.BMI
import com.example.healthmonitor.BmistoreQueries
import com.example.healthmonitor.HeartRate

class BmiRepositoryImpl(private val bmiQueries: BmistoreQueries): BmiRepository {
    override suspend fun addBmi(data: BMI) {
        bmiQueries.addOrUpdate(
            id = null,
            dateTime = data.dateTime,
            weight = data.weight,
            height = data.height,
            bmiValue = data.bmiValue,
            age = data.age,
            sex = data.sex
        )
    }

    override suspend fun updateBmi(data: BMI) {
        bmiQueries.addOrUpdate(
            id = data.id,
            dateTime = data.dateTime,
            weight = data.weight,
            height = data.height,
            bmiValue = data.bmiValue,
            age = data.age,
            sex = data.sex
        )
    }

    override suspend fun getAllBmi(): List<BMI> {
        return bmiQueries.getAllBmi().executeAsList()
    }

    override suspend fun deleteBmi(id: Long) {
        return bmiQueries.deleteBmiById(id)
    }

    override suspend fun getLastBmi(): BMI {
        return bmiQueries.getLastBmi().executeAsOne()
    }
}