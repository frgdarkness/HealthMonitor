package com.example.healthmonitor.ui.bmi.analysis

data class BmiRangeItem(
    val color: String,
    val name: String,
    val range: String
)


enum class BmiRangeEnum(
    val displayName: String,
    val detail: String,
    val min: Float,
    val max: Float,
    val color: String
) {
    UNDERWEIGHT_III("Very severely underweight", "BMI <16.0", 0f, 15.9f, "#FF673AB7"),
    UNDERWEIGHT_II("Severely underweight", "BMI 16.0-16.9", 16f, 16.9f, "#FF3F51B5"),
    UNDERWEIGHT_I("Underweight", "BMI 17.0-18.4", 17f, 18.4f, "#FF00BCD4"),
    NORMAL("Normal", "BMI 18.5-24.9", 18.5f, 24.9f, "#4CAF50"),
    OVERWEIGHT("Overweight", "BMI 25.0-29.9", 25f, 29.9f, "#FFFFEB3B"),
    OBESE_CLASS_I("Obese class I", "BMI 30.0-34.9", 30f, 34.9f, "#FFFFC107"),
    OBESE_CLASS_II("Obese class II", "BMI 35.0-39.9", 0f, 16f, "#FFCA7900"),
    OBESE_CLASS_III("Obese class III", "BMI >=40", 0f, 16f, "#FFFF5722"),
}

fun Float.getBmiRange(): BmiRangeEnum {
    for (range in BmiRangeEnum.values()) {
        if (this in range.min..range.max) return range
    }
    return BmiRangeEnum.OBESE_CLASS_III
}