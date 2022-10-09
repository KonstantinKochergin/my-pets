package com.example.bottomnavigationexample

import com.example.bottomnavigationexample.utils.DateTimeUtils
import org.junit.Test
import org.junit.Assert.*
import java.util.*

class DateTimeUtilsTest {

    @Test
    fun test_timeStringToMinutes() {
        assertEquals(60, DateTimeUtils.timeStringToMinutes("01:00"))
        assertEquals(1, DateTimeUtils.timeStringToMinutes("00:01"))
        assertEquals(0, DateTimeUtils.timeStringToMinutes("00:00"))
        assertEquals(720, DateTimeUtils.timeStringToMinutes("12:00"))
        assertEquals(759, DateTimeUtils.timeStringToMinutes("12:39"))
        assertEquals(1265, DateTimeUtils.timeStringToMinutes("21:05"))
    }

    @Test
    fun test_minutesToHHMM() {
        assertEquals("1 ч.", DateTimeUtils.minutesToHHMM(60))
        assertEquals("21 ч. 5 м.", DateTimeUtils.minutesToHHMM(1265))
    }

    @Test
    fun test_parseTimeAndDateToMinutes() {
        assertEquals(1, DateTimeUtils.parseTimeAndDateToMinutes("00:01", "01.01.1970"))
        assertEquals(200, DateTimeUtils.parseTimeAndDateToMinutes("03:20", "01.01.1970"))
        assertEquals(1440, DateTimeUtils.parseTimeAndDateToMinutes("00:00", "02.01.1970"))
        assertEquals(1575, DateTimeUtils.parseTimeAndDateToMinutes("02:15", "02.01.1970"))
        assertEquals(2530, DateTimeUtils.parseTimeAndDateToMinutes("18:10", "02.01.1970"))
        //assertEquals(27755170, DateTimeUtils.parseTimeAndDateToMinutes("18:10", "09.10.2022"))
    }

    @Test
    fun test_getFormattedDateString() {
        val date = Date()
        assertEquals("09.10.2022", DateTimeUtils.getFormattedDateString(date))
    }
}