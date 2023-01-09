package com.example.healthmonitor.ui.heartrate

import com.example.healthmonitor.R

enum class HeartRateRangeEnum(
    val displayName: String,
    val pulseMin: Int,
    val pulseMax: Int,
    val status: String,
    val color: String,
    val detailStringId: Int
) {
    SLOW("SLOW", 0, 59, "Pulse less 60 bpm", "#FF00BCD4", R.string.heart_rate_slow),
    NORMAL("NORMAL", 60, 100, "Pulse 60 - 100 bpm", "#4CAF50", R.string.heart_rate_normal),
    FAST("FAST", 0, 59, "Pulse > 100 bpm", "#FFFF5722", R.string.heart_rate_fast),
}

fun Int.getPulseRange() = when {
    this < 60 -> HeartRateRangeEnum.SLOW
    this in 60..100 -> HeartRateRangeEnum.NORMAL
    else -> HeartRateRangeEnum.FAST
}