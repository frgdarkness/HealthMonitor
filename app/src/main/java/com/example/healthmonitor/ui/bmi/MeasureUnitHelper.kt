package com.example.healthmonitor.ui.bmi

enum class HeightMeasureUnit(val displayName: String) {
    METRE("m"),
    CENTIMETER("cm"),
    INCH("in"),
    FEET("ft")
}

enum class WeightMeasureUnit(val displayName: String) {
    KILOGRAM("kg"),
    GRAM("g"),
    POUNDS("p")
}

fun HeightMeasureUnit.convertToMetre(value: Float): Float {
    return when (this) {
        HeightMeasureUnit.METRE -> value
        HeightMeasureUnit.INCH -> value
        HeightMeasureUnit.FEET -> value
        HeightMeasureUnit.CENTIMETER -> value
    }
}

