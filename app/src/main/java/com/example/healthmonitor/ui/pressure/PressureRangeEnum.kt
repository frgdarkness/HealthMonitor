package com.example.healthmonitor.ui.pressure

import com.example.healthmonitor.R

enum class PressureRangeEnum(
    val displayName: String,
    val range: String,
    val minSys: Int,
    val maxSys: Int,
    val minDia: Int,
    val maxDia: Int,
    val detailStringId: Int,
    val color: String
) {
    HYPOTENSION("Hypotension", "SYS < 90 or DIA < 60", 0, 90, 0, 60, R.string.pressure_hypotension,"#FF2196F3"),
    NORMAL("Normal", "SYS 90-119 and 60-79", 90, 119, 60, 79, R.string.pressure_normal,"#4CAF50"),
    ELEVATED("ELEVATED", "SYS 120-129 and 60-79", 120, 129, 60, 79, R.string.pressure_elevated,"#FFFFEB3B"),
    HYPERTENSION1("Hypertension. Stage1", "SYS 130-139 and 80-89", 130, 139, 80, 89,R.string.pressure_hypertension_stage1, "#FFFFC107"),
    HYPERTENSION2("Hypertension. Stage2", "SYS 140-180 and 90-120", 140, 180, 90, 120, R.string.pressure_hypertension_stage2,"#FFCA7900"),
    HYPERTENSION_CRISIS("Hypertensive", "SYS >180 or DIA >120", 180, 300, 120, 79, R.string.pressure_hypertensive,"#FFFF5722"),
}

fun getPressureRange(sys: Int, dia: Int): PressureRangeEnum {
    return when {
        sys < 90 || dia < 60 -> PressureRangeEnum.HYPOTENSION
        sys in 90..119 && dia in 60..79 -> PressureRangeEnum.NORMAL
        sys in 120..129 && dia in 60..79 -> PressureRangeEnum.ELEVATED
        sys in 130..139 || dia in 80..89 -> PressureRangeEnum.HYPERTENSION1
        sys in 140..180 || dia in 90..120 -> PressureRangeEnum.HYPERTENSION2
        else -> PressureRangeEnum.HYPERTENSION_CRISIS
    }
}