package com.example.healthmonitor.ui

import java.text.SimpleDateFormat
import java.util.*

object Helper {

    private const val DATE_TIME_FORMAT_DD_MY_YYYY_HH_MM = "dd-MM-yyyy HH:mm"
    private const val DATE_TIME_FORMAT_FULL_MONTH = "MMMM dd, yyyy"
    private const val DATE_TIME_FORMAT_DD_MY_YYYY= "dd/MM/yyyy"

    fun getCurrentMillis(): Long {
        return System.currentTimeMillis()
    }

    fun convertMillisToDateTime(millis: Long): String {
        val format = SimpleDateFormat(DATE_TIME_FORMAT_DD_MY_YYYY_HH_MM, Locale.getDefault())
        val date = Date(millis)
        return format.format(date)
    }

    fun convertMillisToddMMyyyy(millis: Long): String {
        val format = SimpleDateFormat(DATE_TIME_FORMAT_DD_MY_YYYY_HH_MM, Locale.getDefault())
        val date = Date(millis)
        return format.format(date)
    }

    fun convertMillisToFullMonthName(millis: Long): String {
        val format = SimpleDateFormat(DATE_TIME_FORMAT_FULL_MONTH, Locale.getDefault())
        val date = Date(millis)
        return format.format(date)
    }

    fun convertMillisToHHmm(millis: Long): String {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date(millis)
        return format.format(date)
    }

    fun getCurrentDateTime(): String {
        val format = SimpleDateFormat(DATE_TIME_FORMAT_DD_MY_YYYY_HH_MM, Locale.getDefault())
        val date = Date(System.currentTimeMillis())
        return format.format(date)
    }


}