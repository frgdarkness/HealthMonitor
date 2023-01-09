package com.example.healthmonitor.ui.heartrate.measure

import java.util.Date

data class Measurement<T>(val timestamp: Date, val measurement: T)