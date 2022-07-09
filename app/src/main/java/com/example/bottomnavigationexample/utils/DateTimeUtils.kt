package com.example.bottomnavigationexample.utils

class DateTimeUtils {

    companion object {

        // timeString -> hh:mm
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

        fun getDaysFromDDMMYYYY(dateString: String) : Int {
            return  dateString.split("/")[0].toInt()
        }
    }
}