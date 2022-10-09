package com.example.bottomnavigationexample.utils

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class DateTimeUtils {

    companion object {

        // преобразует строку hh:mm в минуты
        // 01:00 -> 60
        // 00:14 -> 14
        // 03:02 -> 182
        fun timeStringToMinutes(timeString: String) : Int {
            val timeArr = timeString.split(":")
            val hours = timeArr[0]
            val minutes = timeArr[1]
            return hours.toInt() * 60 + minutes.toInt()
        }

        fun daysToMinutes(daysCount: Int) : Int {
            return daysCount * 24 * 60
        }

        fun minutesToHHMM(minutes: Int) : String {
            val hours = minutes / 60
            val restMinutes = minutes - hours * 60
            if (restMinutes > 0) {
                return "$hours ч. $restMinutes м."
            }
            return  "$hours ч."
        }

        // Преобразует строки time (hh:mm) и date(dd.mm.yyyy) в минуты (с 01.01.1970)
        fun parseTimeAndDateToMinutes(time: String, date: String) : Long {
            val dateTimeString = "$date $time"
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm")
            val date = sdf.parse(dateTimeString)
            val seconds = date.time / 1000
            val minutes = seconds / 60
            return  minutes
        }

        fun parseTimeAndDateToMinutes(time: String) : Long {
            val dateString = getFormattedDateString(Date())
            return parseTimeAndDateToMinutes(time, dateString)
        }

        fun formatCalendarNumberValue(value: Int) : String {
            if (value < 10) {
                return  "0$value"
            }
            return  "$value"
        }

        fun getFormattedDateString(date: Date): String {
            val calendar = Calendar.getInstance()
            calendar.time = date
            val day = formatCalendarNumberValue(calendar.get(Calendar.DAY_OF_MONTH))
            val month = formatCalendarNumberValue(calendar.get(Calendar.MONTH) + 1)
            val year = calendar.get(Calendar.YEAR)
            return "${day}.${month}.${year}"
        }

    }
}